################################################################################
# Automatically-generated file. Do not edit!
################################################################################

-include ../makefile.init

RM := rm -rf

# All of the sources participating in the build are defined here
-include sources.mk
-include subdir.mk
-include src/os/unix/subdir.mk
-include src/misc/subdir.mk
-include src/mail/subdir.mk
-include src/http/subdir.mk
-include src/http/modules/perl/subdir.mk
-include src/http/modules/subdir.mk
-include src/event/subdir.mk
-include src/event/modules/subdir.mk
-include src/core/subdir.mk
-include objs/src/os/unix/subdir.mk
-include objs/src/http/subdir.mk
-include objs/src/http/modules/subdir.mk
-include objs/src/event/subdir.mk
-include objs/src/event/modules/subdir.mk
-include objs/src/core/subdir.mk
-include objs/subdir.mk
-include objs/addon/src/subdir.mk
-include objs/addon/ngx_module_echo/subdir.mk
-include objs/addon/ngx_echo_module/subdir.mk
-include objs/addon/nginx-gridfs/subdir.mk
-include objs/addon/nginx-echo/subdir.mk
-include objects.mk

ifneq ($(MAKECMDGOALS),clean)
ifneq ($(strip $(C_DEPS)),)
-include $(C_DEPS)
endif
endif

-include ../makefile.defs

# Add inputs and outputs from these tool invocations to the build variables 

# All Target
all: dwq

# Tool invocations
dwq: $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: MacOS X C Linker'
	gcc  -o "dwq" $(OBJS) $(USER_OBJS) $(LIBS)
	@echo 'Finished building target: $@'
	@echo ' '

# Other Targets
clean:
	-$(RM) $(OBJS)$(C_DEPS)$(EXECUTABLES) dwq
	-@echo ' '

.PHONY: all clean dependents
.SECONDARY:

-include ../makefile.targets
