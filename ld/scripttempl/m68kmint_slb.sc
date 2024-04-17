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
    LONG(0);
    LONG(0); 
    SHORT(0);
  }"}
  .text : SUBALIGN(2)
  {
    ${RELOCATING+.text_start = .;}

    /* slb_head + slb_exports */
    KEEP(*(.slb_head))
    LONG((.slb_export_end - .slb_export_start) / 4);
    .slb_export_start = .;
    KEEP(*(SORT(.gnu.linkonce.slb_export.*)))
    .slb_export_end = .;

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
  /DISCARD/ : /* thread local storage not allowed in slb's */
  {
    *(.tdata) *(.tdata.*)
    *(.tbss) *(.tbss.*)
  }
  .bss . : SUBALIGN(2)
  {
    ${RELOCATING+.bss_start = .;}
    *(.bss) *(COMMON) *(.bss.*) *(.gnu.linkonce.b.*)
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
