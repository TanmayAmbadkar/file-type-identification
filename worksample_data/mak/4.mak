#----------------------------------------------------------------------------*
#                                                                            *
# Do NOT edit this makefile with an editor which replaces tabs by spaces !   *    
#                                                                            *
#----------------------------------------------------------------------------*

COMMON_C_CPP_OPTIONS :=

#----------------------------------------------------------------------------*
#                                                                            *
#          Include development board definitions                             *
#                                                                            *
#----------------------------------------------------------------------------*

include $(DEV_DIR)/development-boards/$(DEVELOPMENT_BOARD).txt

#----------------------------------------------------------------------------*

include $(DEV_DIR)/microcontrollers/$(MICROCONTROLLER)-microcontroller.txt

#----------------------------------------------------------------------------*
#                                                                            *
#          Build directories                                                 *
#                                                                            *
#----------------------------------------------------------------------------*

include $(DEV_DIR)/makefiles/build-dirs-definition.mak

#----------------------------------------------------------------------------*
#                                                                            *
#          Build script file for linking in internal ram                     *
#                                                                            *
#----------------------------------------------------------------------------*

LDSCRIPT_INTERNAL_RAM :=
ifdef LINK_FOR_INTERNAL_RAM
  LDSCRIPT_INTERNAL_RAM := $(BUILD_DIR)/linker-script-for-internal-ram.ld
  $(shell mkdir -p $(BUILD_DIR))
  $(shell sed "s/INTERNAL_RAM_SIZE/$(INTERNAL_RAM_SIZE)/g" $(DEV_DIR)/ld-scripts/sample-code-in-internal-ram.template.ld > $(LDSCRIPT_INTERNAL_RAM))
endif

#----------------------------------------------------------------------------*
#                                                                            *
#          Build script file for linking in internal flash                   *
#                                                                            *
#----------------------------------------------------------------------------*

LDSCRIPT_INTERNAL_FLASH :=
ifdef LINK_FOR_INTERNAL_FLASH
  LDSCRIPT_INTERNAL_FLASH := $(BUILD_DIR)/linker-script-for-internal-flash.ld
  $(shell mkdir -p $(BUILD_DIR))
  $(shell sed "s/INTERNAL_RAM_SIZE/$(INTERNAL_RAM_SIZE)/g" $(DEV_DIR)/ld-scripts/sample-code-in-internal-flash.template.ld > $(BUILD_DIR)/temp)
  $(shell sed "s/INTERNAL_FLASH_SIZE/$(INTERNAL_FLASH_SIZE)/g" $(BUILD_DIR)/temp > $(LDSCRIPT_INTERNAL_FLASH))
  $(shell rm $(BUILD_DIR)/temp)
endif

#----------------------------------------------------------------------------*
#                                                                            *
#          Build script file for linking in external ram                     *
#                                                                            *
#----------------------------------------------------------------------------*

LDSCRIPT_EXTERNAL_RAM :=
ifdef LINK_FOR_EXTERNAL_RAM
  LDSCRIPT_EXTERNAL_RAM := $(BUILD_DIR)/linker-script-for-external-ram.ld
  $(shell mkdir -p $(BUILD_DIR))
  $(shell sed "s/INTERNAL_RAM_SIZE/$(INTERNAL_RAM_SIZE)/g" $(DEV_DIR)/ld-scripts/sample-code-in-external-ram.template.ld > $(BUILD_DIR)/temp)
  $(shell sed "s/EXTERNAL_RAM_ADDRESS/$(EXTERNAL_RAM_ADDRESS)/g" $(BUILD_DIR)/temp > $(BUILD_DIR)/temp2)
  $(shell sed "s/EXTERNAL_RAM_SIZE/$(EXTERNAL_RAM_SIZE)/g" $(BUILD_DIR)/temp2 > $(LDSCRIPT_EXTERNAL_RAM))
  $(shell rm $(BUILD_DIR)/temp $(BUILD_DIR)/temp2)
endif

#----------------------------------------------------------------------------*
#                                                                            *
#          XTR handling                                                      *
#                                                                            *
#----------------------------------------------------------------------------*

ifdef XTR_CLASS
 ifeq ($(XTR_CLASS), 0)
  COMMON_C_CPP_OPTIONS += -DXTR_CLASS=0
 else
  ifeq ($(XTR_CLASS), 1)
   COMMON_C_CPP_OPTIONS += -DXTR_CLASS=1
  else
   ifeq ($(XTR_CLASS), 2)
    COMMON_C_CPP_OPTIONS += -DXTR_CLASS=2
   else
    ifeq ($(XTR_CLASS), 3)
     COMMON_C_CPP_OPTIONS += -DXTR_CLASS=3
    else
     $(error Error: XTR_CLASS is equal to $(XTR_CLASS), but it should be 0, 1, 2 or 3)
    endif
   endif
  endif
 endif
else # XTR_CLASS is not defined
 COMMON_C_CPP_OPTIONS += -DXTR_CLASS=0
endif

#----------------------------------------------------------------------------*

include $(DEV_DIR)/makefiles/common-definitions.mak

#----------------------------------------------------------------------------*
#                                                                            *
#          build dispatcher file and inference rule                          *
#                                                                            *
#----------------------------------------------------------------------------*

DISPATCHER_SOURCE_FILE := monitor-dispatcher-definition.s

#----------------------------------------------------------------------------*

ifdef DISPATCHER
  SOURCES += $(DISPATCHER_SOURCE_FILE)
else
  DISPATCHER :=
endif

#----------------------------------------------------------------------------*

MAKE_DISPATCHER_TARGET := $(BUILD_DIR)/$(DISPATCHER_SOURCE_FILE)

#----------------------------------------------------------------------------*

MAKE_DISPATCHER_ARGUMENTS := output-file:$(MAKE_DISPATCHER_TARGET)
MAKE_DISPATCHER_ARGUMENTS += processor:arm7tdmi
ifdef XTR_CLASS
  MAKE_DISPATCHER_ARGUMENTS += defined-variable:XTR_CLASS=$(XTR_CLASS)
endif
MAKE_DISPATCHER_ARGUMENTS += generate-dependency-file:$(BUILD_DIR)/make-dispatcher-dependencies.dep
MAKE_DISPATCHER_ARGUMENTS += $(patsubst %, source-header:%, $(DISPATCHER))

#----------------------------------------------------------------------------*

$(MAKE_DISPATCHER_TARGET):$(DISPATCHER)
	@echo "*** Making monitor dispatcher"
	$(SILENT_CHAR)/usr/local/dev-arm/arm-tools/bin/make-dispatcher $(MAKE_DISPATCHER_ARGUMENTS)  
#----------------------------------------------------------------------------*
#                                                                            *
#          Command files to handle                                           *
#                                                                            *
#----------------------------------------------------------------------------*

COMMAND_FILES_TO_BUILD := 2-run-openocd.command

ifneq ($(LDSCRIPT_INTERNAL_FLASH), )
  COMMAND_FILES_TO_BUILD += 2a-internal-flash-erase.command
  COMMAND_FILES_TO_BUILD += 2a-internal-flash-erase-check.command
  COMMAND_FILES_TO_BUILD += 2a-internal-flash-perform-programming.command
endif

ifneq ($(LDSCRIPT_EXTERNAL_RAM), )
  COMMAND_FILES_TO_BUILD += 2a-run-external-ram.command
  COMMAND_FILES_TO_BUILD += 2a-debug-external-ram.command
endif

ifneq ($(LDSCRIPT_INTERNAL_RAM), )
  COMMAND_FILES_TO_BUILD += 2a-run-internal-ram.command
  COMMAND_FILES_TO_BUILD += 2a-debug-internal-ram.command
endif

COMMAND_FILES_TO_BUILD += clean.command
COMMAND_FILES_TO_BUILD += 1-verbose-build.command
COMMAND_FILES_TO_BUILD += 2b-telnet.command
COMMAND_FILES_TO_BUILD += display-obj-size.command

ifdef SERIAL_DEFINITION
  ifneq ($(SERIAL_DEFINITION), )
    COMMAND_FILES_TO_BUILD += serial.command
  endif
endif

#----------------------------------------------------------------------------*

COMMAND_FILES_TO_DELETE := 2-run-openocd.command
COMMAND_FILES_TO_DELETE += 2a-debug-internal-ram.command
COMMAND_FILES_TO_DELETE += 2a-debug-external-ram.command
COMMAND_FILES_TO_DELETE += 2a-internal-flash-erase.command
COMMAND_FILES_TO_DELETE += 2a-internal-flash-erase-check.command
COMMAND_FILES_TO_DELETE += 2a-internal-flash-perform-programming.command
COMMAND_FILES_TO_DELETE += 2a-run-external-ram.command
COMMAND_FILES_TO_DELETE += 2a-run-internal-ram.command
COMMAND_FILES_TO_DELETE += 2b-telnet.command
COMMAND_FILES_TO_DELETE += display-obj-size.command

#----------------------------------------------------------------------------*
#                                                                            *
#          GDB script files to handle                                        *
#                                                                            *
#----------------------------------------------------------------------------*

GDB_SCRIPT_FILES_TO_BUILD :=
ifneq ($(LDSCRIPT_INTERNAL_FLASH), )
  GDB_SCRIPT_FILES_TO_BUILD += $(GDB_SCRIPT_DIR)/internal-flash-erase.gdb
  GDB_SCRIPT_FILES_TO_BUILD += $(GDB_SCRIPT_DIR)/internal-flash-erase-check.gdb
  GDB_SCRIPT_FILES_TO_BUILD += $(GDB_SCRIPT_DIR)/internal-flash-perform-programming.gdb
endif

ifneq ($(LDSCRIPT_EXTERNAL_RAM), )
  GDB_SCRIPT_FILES_TO_BUILD += $(GDB_SCRIPT_DIR)/debug-in-external-ram.gdb
  GDB_SCRIPT_FILES_TO_BUILD += $(GDB_SCRIPT_DIR)/run-in-external-ram.gdb
endif

ifneq ($(LDSCRIPT_INTERNAL_RAM), )
  GDB_SCRIPT_FILES_TO_BUILD += $(GDB_SCRIPT_DIR)/debug-in-internal-ram.gdb
  GDB_SCRIPT_FILES_TO_BUILD += $(GDB_SCRIPT_DIR)/run-in-internal-ram.gdb
endif

#----------------------------------------------------------------------------*
#                                                                            *
#          make all inference rule                                           *
#                                                                            *
#----------------------------------------------------------------------------*

BUILD_INTERNAL_RAM_IMAGE: $(PRODUCT_DIR)/$(PROJECT)-internal-ram.elf

BUILD_EXTERNAL_RAM_IMAGE: $(PRODUCT_DIR)/$(PROJECT)-external-ram.elf

BUILD_INTERNAL_FLASH_IMAGE: $(PRODUCT_DIR)/$(PROJECT)-internal-flash.elf

OBJECTS := $(patsubst %, $(BUILD_DIR)/%.o, $(SOURCES))
ifdef SOURCES_THUMB
  OBJECTS += $(patsubst %, $(BUILD_DIR)/%.oo, $(SOURCES_THUMB))
endif

MAKE_ALL_TARGETS += $(GDB_SCRIPT_DIR)
MAKE_ALL_TARGETS += $(BUILD_DIR)
MAKE_ALL_TARGETS += $(OBJECTS)

MAKE_ALL_TARGETS += $(PRODUCT_DIR)

ifneq ($(LDSCRIPT_INTERNAL_RAM), )
  MAKE_ALL_TARGETS += BUILD_INTERNAL_RAM_IMAGE
endif

ifneq ($(LDSCRIPT_EXTERNAL_RAM), )
  MAKE_ALL_TARGETS += BUILD_EXTERNAL_RAM_IMAGE
endif

ifneq ($(LDSCRIPT_INTERNAL_FLASH), )
  MAKE_ALL_TARGETS += BUILD_INTERNAL_FLASH_IMAGE
endif

MAKE_ALL_TARGETS += $(GDB_SCRIPT_FILES_TO_BUILD)
MAKE_ALL_TARGETS += $(COMMAND_FILES_TO_BUILD)

#----------------------------------------------------------------------------*
#                                                                            *
#          A L L    I N F E R E N C E    R U L E                             *
#                                                                            *
#----------------------------------------------------------------------------*

all: $(MAKE_ALL_TARGETS)
	@echo "Success !"

#----------------------------------------------------------------------------*
#                                                                            *
#          Command file inference rule                                       *
#                                                                            *
#----------------------------------------------------------------------------*
# Define SERIAL_DEFINITION as an empty variable if not defined

ifndef SERIAL_DEFINITION
  SERIAL_DEFINITION :=
endif

#----------------------------------------------------------------------------*

%.command: $(DEV_DIR)/command-templates/%.command makefile.mak
	@echo "*** Building Command file: $@"
	$(SILENT_CHAR)sed "s:GDBDIR:$(GDB_SCRIPT_DIR):g" $< | \
sed "s:OPENOCDSCRIPTDIR:$(DEV_DIR)/openocd-scripts:g" | \
sed "s@SERIALLINK@$(SERIAL_DEFINITION)@g" | \
sed "s:GDB_TOOL:$(GDB_TOOL):g" | \
sed "s:GNUARMPATH:$(GNU_ARM_PATH):g" | \
sed "s:OBJECTFILELIST:$(OBJECTS):g" | \
sed "s:DEVELOPMENTBOARD:$(strip $(DEVELOPMENT_BOARD)):g" | \
sed "s:CRYSTALFREQUENCY:$(strip $(LPC22XX_CRYSTAL_FREQUENCY)):g" | \
sed "s:PRODUCT_PATH:$(PRODUCT_DIR)/$(PROJECT):g" > $@
	$(SILENT_CHAR)chmod +x $@

#----------------------------------------------------------------------------*

$(GDB_SCRIPT_DIR)/%.gdb: $(DEV_DIR)/gdb-script-templates/%.template.gdb
	@echo "*** Building '$@'"
	$(SILENT_CHAR)sed "s:INTERNAL_FLASH_TARGET:$(PRODUCT_DIR)/$(PROJECT)-internal-flash.elf:g" $< | \
sed "s:INTERNAL_RAM_TARGET:$(PRODUCT_DIR)/$(PROJECT)-internal-ram.elf:g" | \
sed "s:EXTERNAL_RAM_TARGET:$(PRODUCT_DIR)/$(PROJECT)-external-ram.elf:g" | \
sed "s:LAST_INTERNAL_FLASH_BANK:$(LAST_INTERNAL_FLASH_BANK):g" | \
sed "s:SOURCE_DIR_LIST:$(SOURCE_DIR):g" > $@

#----------------------------------------------------------------------------*
#                                                                            *
#          make directory inference rules                                    *
#                                                                            *
#----------------------------------------------------------------------------*

$(BUILD_DIR):
	@echo "*** Making directory: $@"
	$(SILENT_CHAR)mkdir $(BUILD_DIR)

$(PRODUCT_DIR):
	@echo "*** Making directory: $@"
	$(SILENT_CHAR)mkdir $(PRODUCT_DIR)

$(GDB_SCRIPT_DIR):
	@echo "*** Making directory: $@"
	$(SILENT_CHAR)mkdir $(GDB_SCRIPT_DIR)

#----------------------------------------------------------------------------*
#                                                                            *
#          link inference rules                                              *
#                                                                            *
#----------------------------------------------------------------------------*

LDFLAGS_INTERNAL_RAM := $(COMMON_LD_FLAGS) -T$(LDSCRIPT_INTERNAL_RAM)

#----------------------------------------------------------------------------*

LDFLAGS_EXTERNAL_RAM := $(COMMON_LD_FLAGS) -T$(LDSCRIPT_EXTERNAL_RAM)

#----------------------------------------------------------------------------*

LDFLAGS_ROM := $(COMMON_LD_FLAGS) -T$(LDSCRIPT_INTERNAL_FLASH)

#----------------------------------------------------------------------------*

$(PRODUCT_DIR)/%internal-ram.elf: $(OBJECTS) $(LDSCRIPT_INTERNAL_RAM)
	@echo "*** Linking $@"
	$(SILENT_CHAR)$(LD_TOOL) $(OBJECTS) $(LDFLAGS_INTERNAL_RAM) -Map=$@.map $(LIBS) -o $@

#----------------------------------------------------------------------------*

$(PRODUCT_DIR)/%external-ram.elf: $(OBJECTS) $(LDSCRIPT_EXTERNAL_RAM)
	@echo "*** Linking $@"
	$(SILENT_CHAR)$(LD_TOOL) $(OBJECTS) $(LDFLAGS_EXTERNAL_RAM) -Map=$@.map $(LIBS) -o $@

#----------------------------------------------------------------------------*

$(PRODUCT_DIR)/%internal-flash.elf: $(OBJECTS) $(LDSCRIPT_INTERNAL_FLASH)
	@echo "*** Linking $@"
	$(SILENT_CHAR)$(LD_TOOL) $(OBJECTS) $(LDFLAGS_ROM) -Map=$@.map $(LIBS) -o $@

#----------------------------------------------------------------------------*
#                                                                            *
#          clean goal                                                        *
#                                                                            *
#----------------------------------------------------------------------------*

clean:
	rm -fr $(API_DIR)
	rm -fr $(BUILD_DIR)
	rm -fr $(PRODUCT_DIR)
	rm -f $(COMMAND_FILES_TO_DELETE)
	rm -fR $(GDB_SCRIPT_DIR)

#----------------------------------------------------------------------------*
#                                                                            *
#         Compiler invocation inference rules                                *
#                                                                            *
#----------------------------------------------------------------------------*

TEMP_SOURCE_DIRS := $(SOURCE_DIR)
include $(DEV_DIR)/makefiles/compiler-calls.mak

#----------------------------------------------------------------------------*
#                                                                            *
#          Weak include of dependency files                                  *
#         (should be the last of the makefile)                               *
#                                                                            *
#----------------------------------------------------------------------------*

-include $(BUILD_DIR)/*.dep

#----------------------------------------------------------------------------*
