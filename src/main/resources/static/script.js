// Функция для определения, находится ли элемент в области видимости
function isElementInViewport(el) {
    const rect = el.getBoundingClientRect();
    return rect.top >= 0 && rect.left >= 0 && rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && rect.right <= (window.innerWidth || document.documentElement.clientWidth);
}

// Функция для обновления активной ссылки в шапке
function updateActiveLink() {
    const sections = document.querySelectorAll('.section'); // Все секции
    const links = document.querySelectorAll('.header a'); // Все ссылки в шапке

    sections.forEach((section, index) => {
        if (isElementInViewport(section)) {
            // Убираем активный класс у всех ссылок
            links.forEach(link => link.classList.remove('active'));
            // Добавляем активный класс на соответствующую ссылку
            links[index].classList.add('active');
        }
    });
}

// Обработчик события прокрутки
window.addEventListener('scroll', updateActiveLink);

// Ставим "Главная" активной при загрузке страницы
updateActiveLink();

// Ответы на вопросы
const faqItems = document.querySelectorAll('.faq-item');

faqItems.forEach(item => {
    const toggleButton = item.querySelector('.faq-toggle');
    const answer = item.querySelector('.faq-answer');
    
    toggleButton.addEventListener('click', () => {
        const isActive = item.classList.toggle('active'); // Переключаем класс 'active'
        
        if (isActive) {
            toggleButton.style.backgroundImage = "url('/img/minus.svg')"; // Меняем иконку на минус
            toggleButton.style.opacity = "0.6"; // Устанавливаем прозрачность
            answer.style.display = 'block'; // Показываем ответ
        } else {
            toggleButton.style.backgroundImage = "url('/img/plus.svg')"; // Меняем иконку на плюс
            toggleButton.style.opacity = "1"; // Убираем прозрачность
            answer.style.display = 'none'; // Скрываем ответ
        }
    });
})
// МОДАЛЬНОЕ ОКНО
// Получаем элементы
const modal = document.getElementById('modal');
const openModalBtn = document.getElementById('openModalBtn');
const closeModalBtn = document.getElementById('closeModalBtn');

// Открытие модального окна
openModalBtn.addEventListener('click', () => {
    modal.style.display = 'block'; // Показываем модальное окно
});

// Закрытие модального окна
closeModalBtn.addEventListener('click', () => {
    modal.style.display = 'none';
});


// ОТПРАВКА ФАЙЛА СЕРВЕРУ
const uploadButton = document.getElementById('upload-button');
const fileInput = document.getElementById('file-input');
const languageSelect = document.getElementById('language'); // Получаем выпадающий список языка

// Открытие диалога выбора файла по клику на кнопку
uploadButton.addEventListener('click', () => {
  fileInput.click();
});

// Обработка выбора файла
fileInput.addEventListener('change', async () => {
  const file = fileInput.files[0]; // Получаем выбранный файл
  if (!file) return;

  const selectedLanguage = languageSelect.value; // Получаем выбранный язык

  try {
    // Отправляем файл и язык
    const taskId = await sendFileWithLanguage(file, selectedLanguage);
    console.log(`Файл отправлен. ID задачи: ${taskId}`);

    // Запускаем опрос статуса задачи
    await pollTaskStatus(taskId);

  } catch (error) {
    console.error(`Ошибка: ${error.message}`);
    alert('Не удалось завершить операцию. Проверьте соединение или данные.');
  }
});

// Функция для отправки файла и языка
async function sendFileWithLanguage(file, language) {
    const url = `http://localhost:8080/api/v1/transcription`;

    try {
        const fileBase64 = await convertFileToBase64(file);
        const data = { file: fileBase64 };

        console.log('Отправляемые данные:', JSON.stringify(data));

        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data),
        });

        console.log('Ответ сервера (сырые данные):', response);

        if (!response.ok) {
            throw new Error(`Ошибка отправки файла: ${response.statusText}`);
        }

        const responseData = await response.json();
        console.log('Ответ сервера (парсинг JSON):', responseData);

        const taskId = responseData.taskId;
        if (!taskId) {
            throw new Error('Сервер не вернул taskId');
        }

        console.log('Переход на следующую страницу с taskId:', taskId);
        window.location.href = `/result.html?taskId=${encodeURIComponent(taskId)}`;
    } catch (error) {
        console.error(`Ошибка: ${error.message}`);
    }
}


// Преобразование файла в base64
function convertFileToBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onloadend = () => resolve(reader.result.split(',')[1]);
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}


// Функция для опроса статуса задачи
async function pollTaskStatus(taskId) {
    const statusUrl = `http://localhost:8080/api/v1/transcription/${taskId}`;

    const intervalId = setInterval(async () => {
        try {
            const response = await fetch(statusUrl);

            if (!response.ok) {
                throw new Error(`Ошибка проверки статуса: ${response.statusText}`);
            }

            const data = await response.json();

            console.log(`Статус задачи ${taskId}: ${data.status}`);

            // Если задача завершена, останавливаем опрос
            if (data.status === 'completed') {
                console.log(`Задача ${taskId} завершена. Результат: ${data.taskResult}`);
                clearInterval(intervalId);
            }

        } catch (error) {
            console.error(`Ошибка: ${error.message}`);
            clearInterval(intervalId); // Останавливаем, если ошибка критическая
        }
    }, 10000); // Опрос каждые 10 секунд
}
