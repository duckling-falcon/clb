################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/core/nginx.c \
../src/core/ngx_array.c \
../src/core/ngx_buf.c \
../src/core/ngx_conf_file.c \
../src/core/ngx_connection.c \
../src/core/ngx_cpuinfo.c \
../src/core/ngx_crc32.c \
../src/core/ngx_crypt.c \
../src/core/ngx_cycle.c \
../src/core/ngx_file.c \
../src/core/ngx_hash.c \
../src/core/ngx_inet.c \
../src/core/ngx_list.c \
../src/core/ngx_log.c \
../src/core/ngx_md5.c \
../src/core/ngx_murmurhash.c \
../src/core/ngx_open_file_cache.c \
../src/core/ngx_output_chain.c \
../src/core/ngx_palloc.c \
../src/core/ngx_parse.c \
../src/core/ngx_queue.c \
../src/core/ngx_radix_tree.c \
../src/core/ngx_rbtree.c \
../src/core/ngx_regex.c \
../src/core/ngx_resolver.c \
../src/core/ngx_shmtx.c \
../src/core/ngx_slab.c \
../src/core/ngx_spinlock.c \
../src/core/ngx_string.c \
../src/core/ngx_times.c 

OBJS += \
./src/core/nginx.o \
./src/core/ngx_array.o \
./src/core/ngx_buf.o \
./src/core/ngx_conf_file.o \
./src/core/ngx_connection.o \
./src/core/ngx_cpuinfo.o \
./src/core/ngx_crc32.o \
./src/core/ngx_crypt.o \
./src/core/ngx_cycle.o \
./src/core/ngx_file.o \
./src/core/ngx_hash.o \
./src/core/ngx_inet.o \
./src/core/ngx_list.o \
./src/core/ngx_log.o \
./src/core/ngx_md5.o \
./src/core/ngx_murmurhash.o \
./src/core/ngx_open_file_cache.o \
./src/core/ngx_output_chain.o \
./src/core/ngx_palloc.o \
./src/core/ngx_parse.o \
./src/core/ngx_queue.o \
./src/core/ngx_radix_tree.o \
./src/core/ngx_rbtree.o \
./src/core/ngx_regex.o \
./src/core/ngx_resolver.o \
./src/core/ngx_shmtx.o \
./src/core/ngx_slab.o \
./src/core/ngx_spinlock.o \
./src/core/ngx_string.o \
./src/core/ngx_times.o 

C_DEPS += \
./src/core/nginx.d \
./src/core/ngx_array.d \
./src/core/ngx_buf.d \
./src/core/ngx_conf_file.d \
./src/core/ngx_connection.d \
./src/core/ngx_cpuinfo.d \
./src/core/ngx_crc32.d \
./src/core/ngx_crypt.d \
./src/core/ngx_cycle.d \
./src/core/ngx_file.d \
./src/core/ngx_hash.d \
./src/core/ngx_inet.d \
./src/core/ngx_list.d \
./src/core/ngx_log.d \
./src/core/ngx_md5.d \
./src/core/ngx_murmurhash.d \
./src/core/ngx_open_file_cache.d \
./src/core/ngx_output_chain.d \
./src/core/ngx_palloc.d \
./src/core/ngx_parse.d \
./src/core/ngx_queue.d \
./src/core/ngx_radix_tree.d \
./src/core/ngx_rbtree.d \
./src/core/ngx_regex.d \
./src/core/ngx_resolver.d \
./src/core/ngx_shmtx.d \
./src/core/ngx_slab.d \
./src/core/ngx_spinlock.d \
./src/core/ngx_string.d \
./src/core/ngx_times.d 


# Each subdirectory must supply rules for building sources it contributes
src/core/%.o: ../src/core/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


