pipeline {
    agent any

    triggers {
        // Cenario 4 (nightly): executa a cada 15 minutos.
        cron('H/15 * * * *')
    }

    stages {
        stage('Build (container)') {
            steps {
                // Constroi a imagem do estagio de build a partir do Dockerfile.build.
                bat 'docker build -f Dockerfile.build -t calculadora-build:%BUILD_NUMBER% .'

                // Executa o container de build, montando o workspace do Jenkins em /app.
                // O JAR e as classes compiladas ficam em %WORKSPACE%\target.
                bat 'docker run --rm -v "%WORKSPACE%":/app calculadora-build:%BUILD_NUMBER%'
            }
        }

        stage('Test (container)') {
            steps {
                // Constroi a imagem do estagio de teste a partir do Dockerfile.test.
                bat 'docker build -f Dockerfile.test -t calculadora-test:%BUILD_NUMBER% .'

                // Executa o container de teste reusando o mesmo workspace.
                // Os relatorios JUnit caem em %WORKSPACE%\target\surefire-reports.
                bat 'docker run --rm -v "%WORKSPACE%":/app calculadora-test:%BUILD_NUMBER%'
            }
            post {
                // Publica o relatorio JUnit independente do resultado dos testes.
                // E o que permite o build INSTAVEL (amarelo) no Cenario 3.
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}
