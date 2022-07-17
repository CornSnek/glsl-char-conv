get_files = $(wildcard $(1)/*.$(2)) #Directory $(1) with suffix $(2)
get_d = $(filter $(basename $(wildcard $(1)/*)),$(wildcard $(1)/*)) #Get directories (No periods) only by filtering names without suffix.
recursive_search = $(strip $(call get_files,$(1),$(2)) $(foreach dir,$(call get_d,$(1)),$(call $(0),$(dir),$(2)))) #Recusrively search for files with a certain suffix and search subdirectories.
all_classes=$(call recursive_search,.,class) #All classes within this folder and subdirectories to create jar executable.
jar_name=glsl_char_conv
default: ;@echo ----------Compiling classes----------
	javac Main.java
run: ;@echo ----------Running program----------
	java Main
jar_exec: ;@echo ----------Creating jar executable----------
	jar -cvfm $(jar_name).jar manifest.MF $(all_classes)
clean: ;@echo ----------Cleaning directory----------
	rm -f *.class *.jar