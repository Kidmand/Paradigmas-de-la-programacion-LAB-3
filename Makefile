JAVAC = javac
JAVA = java
SRC_DIR = src
OUTPUT_DIR = bin

SPARK_DIR = spark
SPARL_URL = https://dlcdn.apache.org/spark/spark-3.5.1/spark-3.5.1-bin-hadoop3.tgz
SPARK_TAR = spark-3.5.1-bin-hadoop3.tgz

# Obtener todas las bibliotecas JAR en la carpeta lib
LIBS = $(wildcard lib/*.jar)

# Formatear el classpath con todos los archivos JAR y el directorio de salida
CLASSPATH = $(shell echo $(LIBS) | tr ' ' ':')

# Flags para javac
JAVAC_FLAGS = -d $(OUTPUT_DIR) -cp $(CLASSPATH):$(SRC_DIR)

# Encontrar todos los archivos Java en el directorio src
JAVA_FILES = $(shell find $(SRC_DIR) -name "*.java")

# Mapear los archivos .java a archivos .class en el directorio de salida
CLASS_FILES = $(patsubst $(SRC_DIR)/%.java,$(OUTPUT_DIR)/%.class,$(JAVA_FILES))

# Clase principal a ejecutar
MAIN_CLASS = App

# Objetivo por defecto: compilar todos los archivos .class
all: $(CLASS_FILES)

# Regla para compilar archivos .java en archivos .class
$(OUTPUT_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(dir $@)
	$(JAVAC) $(JAVAC_FLAGS) $<

# Objetivo para ejecutar la aplicación
run: $(OUTPUT_DIR)/$(MAIN_CLASS).class
	$(JAVA) -cp $(CLASSPATH):$(OUTPUT_DIR) $(MAIN_CLASS) $(ARGS)

# Objetivo para instalar Spark
spark-install:
	wget $(SPARL_URL)
	mkdir -p $(SPARK_DIR)
	tar -xvzf $(SPARK_TAR) -C $(SPARK_DIR) --strip-components=1
	rm $(SPARK_TAR)
	export SPARK_HOME=$(SPARK_DIR)

# Objetivo para limpiar los archivos compilados
clean:
	rm -rf $(OUTPUT_DIR)/*.class

.PHONY: all run clean