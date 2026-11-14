# FX Enhanced Date Picker

Selector de fechas JavaFX con la fecha elegida siempre visible y un panel de navegación directa. Permite cambiar entre calendario mensual, meses del año, años de la década y décadas del siglo, sin desplazamiento continuo.

## Módulos

- `bom`: versiones centralizadas de JavaFX y JUnit.
- `api`: contrato del modelo y niveles de navegación.
- `core`: modelo observable y operaciones de selección/navegación.
- `controls`: control JavaFX `EnhancedDatePicker`.
- `animations`: transiciones reutilizables.
- `themes`: hoja de estilos CSS predeterminada.
- `demo`: aplicación de ejemplo.

## Requisitos

- JDK 25
- Maven 3.9 o posterior

## Compilar y probar

```shell
mvn clean verify
```

## Ejecutar la demo

```shell
mvn clean install
mvn -pl demo javafx:run
```

## Uso del control

```java
EnhancedDatePicker picker = new EnhancedDatePicker();
```

También se puede inyectar una implementación propia de `DatePickerModel` mediante el constructor.
