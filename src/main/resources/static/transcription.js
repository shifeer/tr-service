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
                document.getElementById('result').textContent = 'Транскрипция все еще выполняется. Попробуйте позже.'; // Информируем пользователя
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

  