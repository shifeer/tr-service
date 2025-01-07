async function fetchTranscriptionResult(taskId) {
    const url = `http://localhost:8080/api/v1/transcription/${encodeURIComponent(taskId)}`;

    try {
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Ошибка получения результата: ${response.statusText}`);
        }

        const data = await response.json();

        // Обрабатываем различные статусы
        if (data.status === 'DONE') {
            document.getElementById('result').textContent = data.taskResult; // Отображаем результат
        } else if (data.status === 'PROCESSING') {
            document.getElementById('result').textContent = 'Транскрипция все еще выполняется.'; // Транскрипция продолжается
        } else if (data.status === 'ERROR') {
            document.getElementById('result').textContent = 'Произошла ошибка при транскрипции.'; // Ошибка
        }
    } catch (error) {
        document.getElementById('result').textContent = `Ошибка: ${error.message}`; // Обработка ошибок
    }
}

async function pollTask(taskId) {
    const url = `http://localhost:8080/api/v1/transcription/${encodeURIComponent(taskId)}`;

    const intervalId = setInterval(async () => {
        try {
            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`Ошибка проверки статуса: ${response.status}`);
            }

            const data = await response.json();

            console.log(`Статус задачи ${taskId}: ${data.status}`);

            // Если задача завершена, останавливаем опрос
            if (data.status === 'DONE') {
                console.log(`Задача ${taskId} завершена. Результат: ${data.taskResult}`);
                document.getElementById('result').textContent = data.taskResult; // Выводим результат на страницу
                clearInterval(intervalId); // Останавливаем опрос
            } else if (data.status === 'PROCESSING') {
                console.log('Транскрипция еще в процессе...');
                document.getElementById('result').textContent = 'Транскрипция всё ещё выполняется.'; // Информируем пользователя
            } else if (data.status === 'ERROR') {
                console.log('Произошла ошибка при транскрипции.');
                document.getElementById('result').textContent = 'Произошла ошибка при транскрипции.'; // Ошибка
                clearInterval(intervalId); // Останавливаем опрос
            }
        } catch (error) {
            console.error(`Ошибка: ${error.message}`);
            document.getElementById('result').textContent = `Ошибка: ${error.message}`; // Обработка ошибок
            clearInterval(intervalId); // Останавливаем опрос в случае ошибки
        }
    }, 10000); // Опрос каждые 10 секунд
}

// Получаем taskId из URL
const params = new URLSearchParams(window.location.search);
const taskId = params.get('taskId');

if (taskId) {
    console.log('taskId из URL:', taskId);
    pollTask(taskId); // Запускаем опрос статуса задачи
} else {
    document.getElementById('result').textContent = 'ID задачи не найден.'; // Если taskId нет в URL
}

// кнопка скопировать
document.querySelector('.copy-btn').addEventListener('click', function() {
    const resultText = document.getElementById('result').textContent;

    // Используем Clipboard API для копирования текста в буфер обмена
    navigator.clipboard.writeText(resultText)
});

// МОДАЛЬНОЕ ОКНО

// Открытие модального окна при нажатии на кнопку "Скачать"
document.querySelector('.download-btn').addEventListener('click', function() {
    document.getElementById('downloadModal').style.display = 'block';
    document.body.classList.add('modal-open'); // Добавляем класс для размытия фона
});

// Закрытие модального окна при клике за его пределами
window.addEventListener('click', function(event) {
    if (event.target === document.getElementById('downloadModal')) {
        document.getElementById('downloadModal').style.display = 'none';
        document.body.classList.remove('modal-open'); // Убираем класс для размытия фона
    }
});

// Функция для скачивания файла
function downloadFile(content, filename, mimeType) {
    const blob = new Blob([content], { type: mimeType });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = filename;
    link.click();
}

document.querySelectorAll('.format-btn').forEach(button => {
    button.addEventListener('click', async function () {
        const format = this.getAttribute('data-format'); // Получаем формат из атрибута кнопки
        const resultText = document.getElementById('result').textContent;
        const body = new URLSearchParams();
        body.append('content', resultText)

        try {
            const response = await fetch(`/files?format=${encodeURIComponent(format)}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: body.toString(),
            });

            if (!response.ok) {
                throw new Error(`Ошибка сервера: ${response.statusText}`);
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `transcription.${format}`;
            a.click();
            window.URL.revokeObjectURL(url);

            document.getElementById('downloadModal').style.display = 'none'; // Закрываем модальное окно
        } catch (error) {
            console.error('Ошибка при скачивании файла:', error);
            alert('Не удалось скачать файл. Попробуйте позже.');
        }
    });
});
