## See sim/Makefile.am
##
## Copyright (C) 2005-2023 Free Software Foundation, Inc.
## Written by Analog Devices, Inc.
##
## This program is free software; you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation; either version 3 of the License, or
## (at your option) any later version.
##
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
##
## You should have received a copy of the GNU General Public License
## along with this program.  If not, see <http://www.gnu.org/licenses/>.

AM_CPPFLAGS_%C% = $(SDL_CFLAGS)

nodist_%C%_libsim_a_SOURCES = \
	%D%/modules.c
%C%_libsim_a_SOURCES = \
	$(common_libcommon_a_SOURCES)
%C%_libsim_a_LIBADD = \
	$(patsubst %,%D%/%,$(SIM_NEW_COMMON_OBJS)) \
	$(patsubst %,%D%/dv-%.o,$(SIM_HW_DEVICES)) \
	$(patsubst %,%D%/dv-%.o,$(%C%_SIM_EXTRA_HW_DEVICES)) \
	%D%/bfin-sim.o \
	%D%/devices.o \
	%D%/gui.o \
	%D%/interp.o \
	%D%/machs.o \
	%D%/sim-resume.o
$(%C%_libsim_a_OBJECTS) $(%C%_libsim_a_LIBADD): %D%/hw-config.h

noinst_LIBRARIES += %D%/libsim.a

## Override wildcards that trigger common/modules.c to be (incorrectly) used.
%D%/modules.o: %D%/modules.c

%D%/%.o: common/%.c ; $(SIM_COMPILE)
-@am__include@ %D%/$(DEPDIR)/*.Po

%C%_run_SOURCES =
%C%_run_LDADD = \
	%D%/nrun.o \
	%D%/libsim.a \
	$(SIM_COMMON_LIBS)

noinst_PROGRAMS += %D%/run

%C%_SIM_EXTRA_HW_DEVICES = \
	bfin_cec \
	bfin_cgu \
	bfin_ctimer \
	bfin_dma \
	bfin_dmac \
	bfin_ebiu_amc \
	bfin_ebiu_ddrc \
	bfin_ebiu_sdc \
	bfin_efs \
	bfin_emac \
	bfin_eppi \
	bfin_evt \
	bfin_gpio \
	bfin_gpio2 \
	bfin_gptimer \
	bfin_jtag \
	bfin_mmu \
	bfin_nfc \
	bfin_otp \
	bfin_pfmon \
	bfin_pint \
	bfin_pll \
	bfin_ppi \
	bfin_rtc \
	bfin_sec \
	bfin_sic \
	bfin_spi \
	bfin_spu \
	bfin_trace \
	bfin_twi \
	bfin_uart \
	bfin_uart2 \
	bfin_uart4 \
	bfin_wdog \
	bfin_wp \
	eth_phy

%D%/linux-fixed-code.h: @MAINT@ $(srcdir)/%D%/linux-fixed-code.s %D%/local.mk %D%/$(am__dirstamp)
	$(AM_V_GEN)$(AS_FOR_TARGET_BFIN) $(srcdir)/%D%/linux-fixed-code.s -o %D%/linux-fixed-code.o
	$(AM_V_at)(\
		set -e; \
		echo "/* DO NOT EDIT: Autogenerated from linux-fixed-code.s.  */"; \
		echo "static const unsigned char bfin_linux_fixed_code[] ="; \
		echo "{"; \
		$(OBJDUMP_FOR_TARGET_BFIN) -d -z %D%/linux-fixed-code.o > $@.dis; \
		sed -n \
			-e 's:^[^	]*	:0x:' \
			-e '/^0x/{s:	.*::;s: *$$:,:;s: :, 0x:g;p;}' \
			$@.dis; \
		rm -f $@.dis; \
		echo "};" \
	) > $@.tmp
	$(AM_V_at)$(SHELL) $(srcroot)/move-if-change $@.tmp $(srcdir)/%D%/linux-fixed-code.h
	$(AM_V_at)touch $(srcdir)/%D%/linux-fixed-code.h
