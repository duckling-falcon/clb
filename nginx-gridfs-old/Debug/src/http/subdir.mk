################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/http/ngx_http.c \
../src/http/ngx_http_busy_lock.c \
../src/http/ngx_http_copy_filter_module.c \
../src/http/ngx_http_core_module.c \
../src/http/ngx_http_file_cache.c \
../src/http/ngx_http_header_filter_module.c \
../src/http/ngx_http_parse.c \
../src/http/ngx_http_parse_time.c \
../src/http/ngx_http_postpone_filter_module.c \
../src/http/ngx_http_request.c \
../src/http/ngx_http_request_body.c \
../src/http/ngx_http_script.c \
../src/http/ngx_http_special_response.c \
../src/http/ngx_http_upstream.c \
../src/http/ngx_http_upstream_round_robin.c \
../src/http/ngx_http_variables.c \
../src/http/ngx_http_write_filter_module.c 

OBJS += \
./src/http/ngx_http.o \
./src/http/ngx_http_busy_lock.o \
./src/http/ngx_http_copy_filter_module.o \
./src/http/ngx_http_core_module.o \
./src/http/ngx_http_file_cache.o \
./src/http/ngx_http_header_filter_module.o \
./src/http/ngx_http_parse.o \
./src/http/ngx_http_parse_time.o \
./src/http/ngx_http_postpone_filter_module.o \
./src/http/ngx_http_request.o \
./src/http/ngx_http_request_body.o \
./src/http/ngx_http_script.o \
./src/http/ngx_http_special_response.o \
./src/http/ngx_http_upstream.o \
./src/http/ngx_http_upstream_round_robin.o \
./src/http/ngx_http_variables.o \
./src/http/ngx_http_write_filter_module.o 

C_DEPS += \
./src/http/ngx_http.d \
./src/http/ngx_http_busy_lock.d \
./src/http/ngx_http_copy_filter_module.d \
./src/http/ngx_http_core_module.d \
./src/http/ngx_http_file_cache.d \
./src/http/ngx_http_header_filter_module.d \
./src/http/ngx_http_parse.d \
./src/http/ngx_http_parse_time.d \
./src/http/ngx_http_postpone_filter_module.d \
./src/http/ngx_http_request.d \
./src/http/ngx_http_request_body.d \
./src/http/ngx_http_script.d \
./src/http/ngx_http_special_response.d \
./src/http/ngx_http_upstream.d \
./src/http/ngx_http_upstream_round_robin.d \
./src/http/ngx_http_variables.d \
./src/http/ngx_http_write_filter_module.d 


# Each subdirectory must supply rules for building sources it contributes
src/http/%.o: ../src/http/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


