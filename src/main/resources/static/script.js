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


const faqItems = document.querySelectorAll('.faq-item');

faqItems.forEach(item => {
    const toggleButton = item.querySelector('.faq-toggle');
    const answer = item.querySelector('.faq-answer');
    
    toggleButton.addEventListener('click', () => {
        const isActive = item.classList.toggle('active'); // Переключаем класс 'active'
        
        if (isActive) {
            toggleButton.style.backgroundImage = "url('/static/img/minus.svg')"; // Меняем иконку на минус
            toggleButton.style.opacity = "0.6"; // Устанавливаем прозрачность
            answer.style.display = 'block'; // Показываем ответ
        } else {
            toggleButton.style.backgroundImage = "url('/static/img/plus.svg')"; // Меняем иконку на плюс
            toggleButton.style.opacity = "1"; // Убираем прозрачность
            answer.style.display = 'none'; // Скрываем ответ
        }
    });

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
            window.location.href = 'loading.html';
          } else {
            alert('Ошибка загрузки файла на сервер');
          }
        })
        .catch((error) => {
          console.error('Произошла ошибка:', error);
          alert('Не удалось подключиться к серверу');
        });
    });
});

