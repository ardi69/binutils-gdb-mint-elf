cat <<EOF
${RELOCATING+OUTPUT_FORMAT(${OUTPUT_FORMAT})}
${RELOCATING-OUTPUT_FORMAT(${RELOCATEABLE_OUTPUT_FORMAT})}
SECTIONS
{
  ${RELOCATING+".mint_prg_info (INFO) :
  {
    SHORT(0x601a);
    LONG(.data_start - .text_start );
    LONG(.bss_start - .data_start );
    LONG(.prg_end - .bss_start );
    LONG(0);
    LONG($MINT);
    LONG(.startup_size); /* needed and setted by tostool */
    SHORT(0);
  }"}
  .text : SUBALIGN(2)
  {
    ${RELOCATING+.text_start = .;}

    ${EXTRA_HEADER}

    .entry = .;
    KEEP(*(.text.entry.mint)) KEEP(*crt0*.o(.text))
    .startup_size = . - .entry;
    *(.text)
    *(.text.unlikely .text.*_unlikely)
    *(.text.exit .text.exit.*)
    *(.text.startup .text.startup.*)
    *(.text.hot .text.hot.*)
    *(.stub .text.* .gnu.linkonce.t.*)
    *(.rodata .rodata.*)

    /* .ctors */
    . = ALIGN(4);
    ___CTOR_LIST__ = .;
    LONG((___CTOR_END__ - ___CTOR_LIST__) / 4 - 2);
    KEEP (*(.ctors))
    KEEP (*(SORT(.ctors.*)))
    LONG(0);
    ___CTOR_END__ = .;

    /* .dtors */
    ___DTOR_LIST__ = .;
    LONG((___DTOR_END__ - ___DTOR_LIST__) / 4 - 2);
    KEEP (*(.dtors))
    KEEP (*(SORT(.dtors.*)))
    LONG(0);
    ___DTOR_END__ = .;
    ___EH_FRAME_BEGIN__ = . ;
    KEEP(*(.eh_frame))
    ___EH_FRAME_END__ = . ;
    LONG(0);
    ${RELOCATING+_etext = .;}
    ${RELOCATING+__etext = .;}
  }
  .data . : SUBALIGN(2)
  {
    ${RELOCATING+.data_start = .;}
    *(.data) *(.data.*) *(.gnu.linkonce.d.*) *(.gcc_except_table) *(.gcc_except_table.*)
    . = ALIGN(2);
    ${RELOCATING+_edata = .;}
    ${RELOCATING+__edata = .;}
  }
  .tdata . : SUBALIGN(4)
  {
    ___TLS_DATA__ = .;
    *(.tdata) *(.tdata.*)
    . = ALIGN(2);
    ___TLS_DATA_SIZE__ = . - ___TLS_DATA__;
  }
  .tbss . : SUBALIGN(4)
  {
     *(.tbss) *(.tbss.*)
    ___TLS_SIZE__ = . - ___TLS_DATA__;
  }
  .bss . : SUBALIGN(2)
  {
    ${RELOCATING+.bss_start = .;}
    *(.bss) *(COMMON) *(.bss.*) *(.gnu.linkonce.b.*)
    . = ALIGN(4);
    ${RELOCATING+___tls_main = .;}
    . = . + ___TLS_SIZE__;
    . = ALIGN(2);
    ${RELOCATING+_end = .;}
    ${RELOCATING+__end = .;}
    ${RELOCATING+.prg_end = .;}
  }

EOF

. $srcdir/scripttempl/DWARF.sc

cat <<EOF
}
EOF
