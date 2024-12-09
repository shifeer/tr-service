document.addEventListener('DOMContentLoaded', () => {
    const languageButtons = document.querySelectorAll('.language-btn');
  
    languageButtons.forEach(button => {
      button.addEventListener('click', () => {
        // Убираем класс active у всех кнопок
        languageButtons.forEach(btn => btn.classList.remove('active'));
        // Добавляем класс active к нажимаемой кнопке
        button.classList.add('active');
      });
    });
  });

  document.addEventListener('DOMContentLoaded', () => {
    // Инициализация длительности аудио (можно заменить на реальную длительность аудиофайла)
    const audioDurationElement = document.getElementById('audio-duration');
    let audioDuration = "05:41"; // Это пример, заменить на реальную длительность после загрузки
  
    audioDurationElement.textContent = audioDuration;
  
    // Функция для обработки кнопки преобразования
    const startButton = document.getElementById('start-transcription');
    startButton.addEventListener('click', () => {
      // Начать процесс транскрибации
      const language = document.getElementById('language').value;
      startTranscription(language);
    });
  
    // Функция для начала процесса транскрибации
    function startTranscription(language) {
      alert(`Начинается процесс транскрибации для языка: ${language}`);
  
      // Здесь можно реализовать дальнейшую логику, например, отправку данных на сервер
      // Для этого можно использовать fetch или другие методы для работы с сервером
    }
  });
  