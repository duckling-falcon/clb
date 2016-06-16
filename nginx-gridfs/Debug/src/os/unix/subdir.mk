################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/os/unix/ngx_aio_read.c \
../src/os/unix/ngx_aio_read_chain.c \
../src/os/unix/ngx_aio_write.c \
../src/os/unix/ngx_aio_write_chain.c \
../src/os/unix/ngx_alloc.c \
../src/os/unix/ngx_channel.c \
../src/os/unix/ngx_daemon.c \
../src/os/unix/ngx_darwin_init.c \
../src/os/unix/ngx_darwin_sendfile_chain.c \
../src/os/unix/ngx_errno.c \
../src/os/unix/ngx_file_aio_read.c \
../src/os/unix/ngx_files.c \
../src/os/unix/ngx_freebsd_init.c \
../src/os/unix/ngx_freebsd_rfork_thread.c \
../src/os/unix/ngx_freebsd_sendfile_chain.c \
../src/os/unix/ngx_linux_aio_read.c \
../src/os/unix/ngx_linux_init.c \
../src/os/unix/ngx_linux_sendfile_chain.c \
../src/os/unix/ngx_posix_init.c \
../src/os/unix/ngx_process.c \
../src/os/unix/ngx_process_cycle.c \
../src/os/unix/ngx_pthread_thread.c \
../src/os/unix/ngx_readv_chain.c \
../src/os/unix/ngx_recv.c \
../src/os/unix/ngx_send.c \
../src/os/unix/ngx_setaffinity.c \
../src/os/unix/ngx_setproctitle.c \
../src/os/unix/ngx_shmem.c \
../src/os/unix/ngx_socket.c \
../src/os/unix/ngx_solaris_init.c \
../src/os/unix/ngx_solaris_sendfilev_chain.c \
../src/os/unix/ngx_time.c \
../src/os/unix/ngx_udp_recv.c \
../src/os/unix/ngx_user.c \
../src/os/unix/ngx_writev_chain.c 

S_UPPER_SRCS += \
../src/os/unix/rfork_thread.S 

OBJS += \
./src/os/unix/ngx_aio_read.o \
./src/os/unix/ngx_aio_read_chain.o \
./src/os/unix/ngx_aio_write.o \
./src/os/unix/ngx_aio_write_chain.o \
./src/os/unix/ngx_alloc.o \
./src/os/unix/ngx_channel.o \
./src/os/unix/ngx_daemon.o \
./src/os/unix/ngx_darwin_init.o \
./src/os/unix/ngx_darwin_sendfile_chain.o \
./src/os/unix/ngx_errno.o \
./src/os/unix/ngx_file_aio_read.o \
./src/os/unix/ngx_files.o \
./src/os/unix/ngx_freebsd_init.o \
./src/os/unix/ngx_freebsd_rfork_thread.o \
./src/os/unix/ngx_freebsd_sendfile_chain.o \
./src/os/unix/ngx_linux_aio_read.o \
./src/os/unix/ngx_linux_init.o \
./src/os/unix/ngx_linux_sendfile_chain.o \
./src/os/unix/ngx_posix_init.o \
./src/os/unix/ngx_process.o \
./src/os/unix/ngx_process_cycle.o \
./src/os/unix/ngx_pthread_thread.o \
./src/os/unix/ngx_readv_chain.o \
./src/os/unix/ngx_recv.o \
./src/os/unix/ngx_send.o \
./src/os/unix/ngx_setaffinity.o \
./src/os/unix/ngx_setproctitle.o \
./src/os/unix/ngx_shmem.o \
./src/os/unix/ngx_socket.o \
./src/os/unix/ngx_solaris_init.o \
./src/os/unix/ngx_solaris_sendfilev_chain.o \
./src/os/unix/ngx_time.o \
./src/os/unix/ngx_udp_recv.o \
./src/os/unix/ngx_user.o \
./src/os/unix/ngx_writev_chain.o \
./src/os/unix/rfork_thread.o 

C_DEPS += \
./src/os/unix/ngx_aio_read.d \
./src/os/unix/ngx_aio_read_chain.d \
./src/os/unix/ngx_aio_write.d \
./src/os/unix/ngx_aio_write_chain.d \
./src/os/unix/ngx_alloc.d \
./src/os/unix/ngx_channel.d \
./src/os/unix/ngx_daemon.d \
./src/os/unix/ngx_darwin_init.d \
./src/os/unix/ngx_darwin_sendfile_chain.d \
./src/os/unix/ngx_errno.d \
./src/os/unix/ngx_file_aio_read.d \
./src/os/unix/ngx_files.d \
./src/os/unix/ngx_freebsd_init.d \
./src/os/unix/ngx_freebsd_rfork_thread.d \
./src/os/unix/ngx_freebsd_sendfile_chain.d \
./src/os/unix/ngx_linux_aio_read.d \
./src/os/unix/ngx_linux_init.d \
./src/os/unix/ngx_linux_sendfile_chain.d \
./src/os/unix/ngx_posix_init.d \
./src/os/unix/ngx_process.d \
./src/os/unix/ngx_process_cycle.d \
./src/os/unix/ngx_pthread_thread.d \
./src/os/unix/ngx_readv_chain.d \
./src/os/unix/ngx_recv.d \
./src/os/unix/ngx_send.d \
./src/os/unix/ngx_setaffinity.d \
./src/os/unix/ngx_setproctitle.d \
./src/os/unix/ngx_shmem.d \
./src/os/unix/ngx_socket.d \
./src/os/unix/ngx_solaris_init.d \
./src/os/unix/ngx_solaris_sendfilev_chain.d \
./src/os/unix/ngx_time.d \
./src/os/unix/ngx_udp_recv.d \
./src/os/unix/ngx_user.d \
./src/os/unix/ngx_writev_chain.d 


# Each subdirectory must supply rules for building sources it contributes
src/os/unix/%.o: ../src/os/unix/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

src/os/unix/%.o: ../src/os/unix/%.S
	@echo 'Building file: $<'
	@echo 'Invoking: GCC Assembler'
	as  -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


