################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/event/ngx_event.c \
../src/event/ngx_event_accept.c \
../src/event/ngx_event_busy_lock.c \
../src/event/ngx_event_connect.c \
../src/event/ngx_event_mutex.c \
../src/event/ngx_event_openssl.c \
../src/event/ngx_event_pipe.c \
../src/event/ngx_event_posted.c \
../src/event/ngx_event_timer.c 

OBJS += \
./src/event/ngx_event.o \
./src/event/ngx_event_accept.o \
./src/event/ngx_event_busy_lock.o \
./src/event/ngx_event_connect.o \
./src/event/ngx_event_mutex.o \
./src/event/ngx_event_openssl.o \
./src/event/ngx_event_pipe.o \
./src/event/ngx_event_posted.o \
./src/event/ngx_event_timer.o 

C_DEPS += \
./src/event/ngx_event.d \
./src/event/ngx_event_accept.d \
./src/event/ngx_event_busy_lock.d \
./src/event/ngx_event_connect.d \
./src/event/ngx_event_mutex.d \
./src/event/ngx_event_openssl.d \
./src/event/ngx_event_pipe.d \
./src/event/ngx_event_posted.d \
./src/event/ngx_event_timer.d 


# Each subdirectory must supply rules for building sources it contributes
src/event/%.o: ../src/event/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


