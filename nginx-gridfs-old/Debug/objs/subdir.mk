################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
O_SRCS += \
../objs/ngx_modules.o 

C_SRCS += \
../objs/ngx_modules.c 

OBJS += \
./objs/ngx_modules.o 

C_DEPS += \
./objs/ngx_modules.d 


# Each subdirectory must supply rules for building sources it contributes
objs/%.o: ../objs/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


