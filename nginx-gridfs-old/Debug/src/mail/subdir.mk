################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/mail/ngx_mail.c \
../src/mail/ngx_mail_auth_http_module.c \
../src/mail/ngx_mail_core_module.c \
../src/mail/ngx_mail_handler.c \
../src/mail/ngx_mail_imap_handler.c \
../src/mail/ngx_mail_imap_module.c \
../src/mail/ngx_mail_parse.c \
../src/mail/ngx_mail_pop3_handler.c \
../src/mail/ngx_mail_pop3_module.c \
../src/mail/ngx_mail_proxy_module.c \
../src/mail/ngx_mail_smtp_handler.c \
../src/mail/ngx_mail_smtp_module.c \
../src/mail/ngx_mail_ssl_module.c 

OBJS += \
./src/mail/ngx_mail.o \
./src/mail/ngx_mail_auth_http_module.o \
./src/mail/ngx_mail_core_module.o \
./src/mail/ngx_mail_handler.o \
./src/mail/ngx_mail_imap_handler.o \
./src/mail/ngx_mail_imap_module.o \
./src/mail/ngx_mail_parse.o \
./src/mail/ngx_mail_pop3_handler.o \
./src/mail/ngx_mail_pop3_module.o \
./src/mail/ngx_mail_proxy_module.o \
./src/mail/ngx_mail_smtp_handler.o \
./src/mail/ngx_mail_smtp_module.o \
./src/mail/ngx_mail_ssl_module.o 

C_DEPS += \
./src/mail/ngx_mail.d \
./src/mail/ngx_mail_auth_http_module.d \
./src/mail/ngx_mail_core_module.d \
./src/mail/ngx_mail_handler.d \
./src/mail/ngx_mail_imap_handler.d \
./src/mail/ngx_mail_imap_module.d \
./src/mail/ngx_mail_parse.d \
./src/mail/ngx_mail_pop3_handler.d \
./src/mail/ngx_mail_pop3_module.d \
./src/mail/ngx_mail_proxy_module.d \
./src/mail/ngx_mail_smtp_handler.d \
./src/mail/ngx_mail_smtp_module.d \
./src/mail/ngx_mail_ssl_module.d 


# Each subdirectory must supply rules for building sources it contributes
src/mail/%.o: ../src/mail/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


