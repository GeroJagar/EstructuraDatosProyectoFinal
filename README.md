# 📚 Actually - Red Social de Aprendizaje Colaborativo
 
 Plataforma académica que simula una red social educativa donde los estudiantes pueden compartir contenidos, conectarse con compañeros afines, solicitar ayuda, y participar en grupos de estudio generados automáticamente. Proyecto final del curso **Estructuras de Datos**.
 
 ---
 
 ## 🧩 Características Principales
 
 - Registro y autenticación de estudiantes y moderadores.
 - Publicación y valoración de contenidos académicos.
 - Formación automática de grupos de estudio según intereses comunes.
 - Sistema de afinidad entre estudiantes representado con grafos.
 - Gestión de solicitudes de ayuda por prioridad.
 - Recomendaciones de compañeros de estudio (sugerencias).
 - Visualización gráfica del grafo de conexiones (por implementar).
 
 ---
 
 ## 🏗️ Arquitectura Modular
 
 - `Usuario` (abstracto): Clase base para `Estudiante` y `Moderador`.
 - `ContenidoAcademico`: Representa un recurso educativo compartido.
 - `GrupoEstudio`: Agrupa estudiantes con intereses comunes.
 - `SolicitudAyuda`: Modela una petición de apoyo académico con urgencia.
 - `TEMA` (enum): Enumera categorías de estudio disponibles.
 
 ### 🔧 Estructuras de Datos Personalizadas
 
 - Árbol Binario de Búsqueda (ABB) para organización de contenidos.
 - Grafo no dirigido para relaciones de afinidad entre estudiantes.
 
 1. Clona el repositorio:
    ```bash
    git clone https://github.com/GeroJagar/EstructuraDatosProyectoFinal.git
    cd EstructuraDatosProyectoFinal
