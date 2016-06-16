################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/event/modules/ngx_aio_module.c \
../src/event/modules/ngx_devpoll_module.c \
../src/event/modules/ngx_epoll_module.c \
../src/event/modules/ngx_eventport_module.c \
../src/event/modules/ngx_kqueue_module.c \
../src/event/modules/ngx_poll_module.c \
../src/event/modules/ngx_rtsig_module.c \
../src/event/modules/ngx_select_module.c \
../src/event/modules/ngx_win32_select_module.c 

OBJS += \
./src/event/modules/ngx_aio_module.o \
./src/event/modules/ngx_devpoll_module.o \
./src/event/modules/ngx_epoll_module.o \
./src/event/modules/ngx_eventport_module.o \
./src/event/modules/ngx_kqueue_module.o \
./src/event/modules/ngx_poll_module.o \
./src/event/modules/ngx_rtsig_module.o \
./src/event/modules/ngx_select_module.o \
./src/event/modules/ngx_win32_select_module.o 

C_DEPS += \
./src/event/modules/ngx_aio_module.d \
./src/event/modules/ngx_devpoll_module.d \
./src/event/modules/ngx_epoll_module.d \
./src/event/modules/ngx_eventport_module.d \
./src/event/modules/ngx_kqueue_module.d \
./src/event/modules/ngx_poll_module.d \
./src/event/modules/ngx_rtsig_module.d \
./src/event/modules/ngx_select_module.d \
./src/event/modules/ngx_win32_select_module.d 


# Each subdirectory must supply rules for building sources it contributes
src/event/modules/%.o: ../src/event/modules/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


