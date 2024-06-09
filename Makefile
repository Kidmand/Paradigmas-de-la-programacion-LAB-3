JAVAC = javac
JAVA = java
SRC_DIR = src
LIBS = $(wildcard lib/*.jar)
CLASSPATH = $(SRC_DIR):$(LIBS)
JAVAC_FLAGS = -d bin -cp $(CLASSPATH)
JAVA_FILES = $(shell find $(SRC_DIR) -name "*.java")
CLASS_FILES = $(patsubst $(SRC_DIR)/%.java,bin/%.class,$(JAVA_FILES))
MAIN_CLASS = App
OUTPUT_DIR = bin
SPARK_DIR = spark
SPARL_URL = https://dlcdn.apache.org/spark/spark-3.5.1/spark-3.5.1-bin-hadoop3.tgz
SPARK_TAR = spark-3.5.1-bin-hadoop3.tgz


all: $(CLASS_FILES)

bin/%.class: src/%.java
	$(JAVAC) $(JAVAC_FLAGS) $<

run: bin/$(MAIN_CLASS).class
	@$(JAVA) -cp $(LIBS):bin $(MAIN_CLASS) $(ARGS)

spark-install:
	wget $(SPARL_URL)
	mkdir -p $(SPARK_DIR)
	tar -xvzf $(SPARK_TAR) -C $(SPARK_DIR) --strip-components=1
	rm $(SPARK_TAR)
	export SPARK_HOME=$(SPARK_DIR)

clean:
	rm -rf $(OUTPUT_DIR)/*.class

.PHONY: all run clean
