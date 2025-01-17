This is a service for transcribing audio files into text.

How to run the project:
        
        - Clone or download this repository.
        - Create directories in the project root (temp, modelsVosk).
        - Download models with the languages(ru, en) ​​you need to the "modelsVosk" directory (https://alphacephei.com/vosk/models).
        - Add path to models.
        - Also, Redis is required for the service to work. Run it and write your login details in the application.yaml file.
        - Then download ffmpeg and add it to your PATH environment variable.
