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
  const url = `/api/v1/transcription`;

  const formData = new FormData();
  formData.append('file', file);
  formData.append('language', language);

  try {
    console.log('Отправляемые данные:', formData);

    const response = await fetch(url, {
      method: 'POST',
      body: formData,
    });

    if (!response.ok) {
        // Проверяем код ответа
        if (response.status === 404) {
            // Ошибка клиента (не найдено)
            const errorData = await response.json();
            throw new Error(`Ошибка: ${errorData.message || 'Ресурс не найден'}`);
        } else if (response.status === 500) {
            // Ошибка сервера
            throw new Error('Ошибка сервера. Пожалуйста, попробуйте позже.');
        } else {
            // Для других статусов (например, 400, 401 и т.д.)
            throw new Error(`Ошибка отправки файла: ${response.statusText}`);
        }
    }

    const responseData = await response.json();
    console.log('Ответ сервера:', responseData);

    const taskId = responseData.taskId;
    if (!taskId) {
      throw new Error('Сервер не вернул taskId');
    }

    // Перенаправляем на URL с параметром языка
    const redirectUrl = `/transcription?taskId=${taskId}`;
    console.log('Переход на следующую страницу:', redirectUrl);
    window.location.href = redirectUrl;
  } catch (error) {
    console.error(`Ошибка: ${error.message}`);
    throw error;
  }
}

// Функция для опроса статуса задачи
async function pollTaskStatus(taskId) {
  const statusUrl = `/api/v1/transcription/${taskId}`;

  const intervalId = setInterval(async () => {
    try {
      const response = await fetch(statusUrl);

      if (!response.ok) {
        throw new Error(`Ошибка проверки статуса: ${response.status}`);
      }

      const data = await response.json();

      console.log(`Статус задачи ${taskId}: ${data.status}`);

      // Если задача завершена, останавливаем опрос
      if (data.status === 'DONE') {
        console.log(`Задача ${taskId} завершена. Результат: ${data.taskResult}`);
        clearInterval(intervalId);
      }
    } catch (error) {
      console.error(`Ошибка: ${error.message}`);
      clearInterval(intervalId); // Останавливаем, если ошибка критическая
    }
  }, 10000); // Опрос каждые 10 секунд
}
