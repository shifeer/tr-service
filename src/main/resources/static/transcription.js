async function fetchTranscriptionResult(taskId) {
            const url = `http://localhost:8080/api/v1/transcription/${encodeURIComponent(taskId)}`;

            try {
                const response = await fetch(url);

                if (!response.ok) {
                    throw new Error(`Ошибка получения результата: ${response.statusText}`);
                }

                const data = await response.json();

                if (data.status === 'completed') {
                    document.getElementById('result').textContent = data.taskResult;
                } else if (data.status === 'processing') {
                    document.getElementById('result').textContent = 'Транскрипция все еще выполняется. Попробуйте позже.';
                } else if (data.status === 'error') {
                    document.getElementById('result').textContent = 'Произошла ошибка при транскрипции.';
                }
            } catch (error) {
                document.getElementById('result').textContent = `Ошибка: ${error.message}`;
            }
        }

        // Получаем taskId из URL
        const params = new URLSearchParams(window.location.search);
        const taskId = params.get('taskId');

        if (taskId) {
            fetchTranscriptionResult(taskId);
        } else {
            document.getElementById('result').textContent = 'ID задачи не найден.';
        }
  