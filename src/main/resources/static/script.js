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
});

// Выносим обработчики загрузки файла за пределы цикла
const uploadButton = document.getElementById('upload-button');
const fileInput = document.getElementById('file-input');

// Открытие диалога выбора файла по клику на кнопку
uploadButton.addEventListener('click', () => {
    fileInput.click();
});

// Обработка выбора файла
fileInput.addEventListener('change', () => {
    const file = fileInput.files[0]; // Получаем выбранный файл
    if (!file) return;

    // Создаём FormData для отправки файла
    const formData = new FormData();
    formData.append('file', file);

    // Отправляем файл на сервер через fetch
    fetch('http://localhost:8080/api/v1/transcription', { // Адрес эндпоинта от бэкенд-разработчика
        method: 'POST', // Тип HTTP-запроса
        body: formData, // Отправляем файл через FormData
    })
        .then((response) => {
            if (response.ok) {
                // Если файл успешно загружен, переходим на страницу прогресса
                window.location.href = 'transcription';
            } else {
                alert('Ошибка загрузки файла на сервер');
            }
        })
        .catch((error) => {
            console.error('Произошла ошибка:', error);
            alert('Не удалось подключиться к серверу');
        });
});