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

### 🔧 Estructuras de Datos Personalizadas (por desarrollar)

- Árbol Binario de Búsqueda (ABB) para organización de contenidos.
- Grafo no dirigido para relaciones de afinidad entre estudiantes.
- Cola de prioridad para gestionar solicitudes urgentes.
- Listas enlazadas para historial y navegación de contenidos.

---

## 🚀 Tecnologías Utilizadas

- **Java 17+**
- **Lombok** para reducción de código repetitivo
- **Git** para control de versiones
- **JUnit** (futuro) para pruebas unitarias

---

## 🧪 Cómo Ejecutar el Proyecto

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tuusuario/actually.git
   cd actually
