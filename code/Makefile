BUILD_DIR=build
SRC_DIR=src
LIB_DIR=lib
LIBS=$(shell find $(LIB_DIR) -type f -name '*.jar' | awk 1 ORS=':')
JAVAFX_HOME=/usr/lib/jvm/java-11-openjfx/lib
JAVAFX_FLAGS=--module-path $(JAVAFX_HOME) --add-modules javafx.controls,javafx.fxml,javafx.media
JC=javac
JFLAGS=-d $(BUILD_DIR) --class-path $(LIBS)$(SRC_DIR) -g
JVM=java 
JVM_FLAGS=--class-path $(LIBS)$(BUILD_DIR):$(SRC_DIR)

.SUFFIXES: .java .class

SRC_FILES=$(shell find $(SRC_DIR) -type f -name '*.java')
# NOTE: It's replacing all nesting $(SRC_DIR) which might not be wanted
BUILD_SRC_FILES=$(subst $(SRC_DIR),$(BUILD_DIR),$(SRC_FILES))
CLASSES_FILES=$(BUILD_SRC_FILES:.java=.class)

$(CLASSES_FILES): $(BUILD_DIR) $(SRC_FILES)
	$(JC) $(JFLAGS) $(JAVAFX_FLAGS) $(SRC_FILES)

$(BUILD_DIR):
	@mkdir -p $(BUILD_DIR)

.PHONY: run clean
run: $(CLASSES_FILES)
	@$(JVM) $(JVM_FLAGS) $(JAVAFX_FLAGS) gui.BabaIsYouApp

clean:
	$(RM) -r $(BUILD_DIR)/*

