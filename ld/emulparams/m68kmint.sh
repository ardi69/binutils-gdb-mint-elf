. ${srcdir}/emulparams/m68katari.sh

MINT="0x4d694e54 /* MiNT */"
EXTRA_HEADER="$(cat << EOF
LONG(0x203a001a);                      /* g_jump_entry[0] */
    LONG(0x4efb08fa);                  /* g_jump_entry[0] */
    LONG(0x108);                       /* e_info */
    LONG(.data_start - .entry );       /* e_text */
    LONG(.bss_start - .data_start );   /* e_data */
    LONG(.prg_end - .bss_start );      /* e_bss */
    LONG(0);                           /* e_syms */
    LONG(.entry);                      /* e_entry */
    . = . + 0x10;
    LONG(0);                           /* g_stkpos (filled from tostool) */
    LONG(1);                           /* g_symbol_format */
    . = . + 0xAC;
EOF
)"
