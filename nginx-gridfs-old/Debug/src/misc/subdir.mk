################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/misc/ngx_google_perftools_module.c 

OBJS += \
./src/misc/ngx_google_perftools_module.o 

C_DEPS += \
./src/misc/ngx_google_perftools_module.d 


# Each subdirectory must supply rules for building sources it contributes
src/misc/%.o: ../src/misc/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


