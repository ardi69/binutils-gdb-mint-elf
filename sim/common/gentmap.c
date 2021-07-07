/* Generate targ-vals.h and targ-map.c.  */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct tdefs {
  char *symbol;
  int value;
};

static struct tdefs sys_tdefs[] = {
#define sys_defs
#include "nltvals.def"
#undef sys_defs
  { 0, 0 }
};

static struct tdefs signal_tdefs[] = {
#define signal_defs
#include "nltvals.def"
#undef signal_defs
  { 0, 0 }
};

static struct tdefs open_tdefs[] = {
#define open_defs
#include "nltvals.def"
#undef open_defs
  { 0, 0 }
};

static void
gen_targ_vals_h (void)
{
  struct tdefs *t;

  printf ("/* Target header values needed by the simulator and gdb.  */\n");
  printf ("/* This file is machine generated by gentmap.c.  */\n\n");

  printf ("#ifndef TARG_VALS_H\n");
  printf ("#define TARG_VALS_H\n\n");

  printf ("/* syscall values */\n");
  for (t = &sys_tdefs[0]; t->symbol; ++t)
    printf ("#define TARGET_%s %d\n", t->symbol, t->value);
  printf ("\n");

  printf ("/* signal values */\n");
  for (t = &signal_tdefs[0]; t->symbol; ++t)
    printf ("#define TARGET_%s %d\n", t->symbol, t->value);
  printf ("\n");

  printf ("/* open flag values */\n");
  for (t = &open_tdefs[0]; t->symbol; ++t)
    printf ("#define TARGET_%s 0x%x\n", t->symbol, t->value);
  printf ("\n");

  printf ("#endif /* TARG_VALS_H */\n");
}

static void
gen_targ_map_c (void)
{
  struct tdefs *t;

  printf ("/* Target value mapping utilities needed by the simulator and gdb.  */\n");
  printf ("/* This file is machine generated by gentmap.c.  */\n\n");

  printf ("#include \"defs.h\"\n");
  printf ("#include <fcntl.h>\n");
  printf ("#include <signal.h>\n");
  printf ("#include \"ansidecl.h\"\n");
  printf ("#include \"sim/callback.h\"\n");
  printf ("#include \"targ-vals.h\"\n");
  printf ("\n");

  printf ("/* syscall mapping table */\n");
  printf ("CB_TARGET_DEFS_MAP cb_init_syscall_map[] = {\n");
  for (t = &sys_tdefs[0]; t->symbol; ++t)
    {
      printf ("#ifdef CB_%s\n", t->symbol);
      /* Skip the "SYS_" prefix for the name.  */
      printf ("  { \"%s\", CB_%s, TARGET_%s },\n", t->symbol + 4, t->symbol, t->symbol);
      printf ("#endif\n");
    }
  printf ("  { 0, -1, -1 }\n");
  printf ("};\n\n");

  printf ("/* signals mapping table */\n");
  printf ("CB_TARGET_DEFS_MAP cb_init_signal_map[] = {\n");
  for (t = &signal_tdefs[0]; t->symbol; ++t)
    {
      printf ("#ifdef %s\n", t->symbol);
      printf ("  { \"%s\", %s, TARGET_%s },\n", t->symbol, t->symbol, t->symbol);
      printf ("#endif\n");
    }
  printf ("  { 0, -1, -1 }\n");
  printf ("};\n\n");

  printf ("/* open flags mapping table */\n");
  printf ("CB_TARGET_DEFS_MAP cb_init_open_map[] = {\n");
  for (t = &open_tdefs[0]; t->symbol; ++t)
    {
      printf ("#ifdef %s\n", t->symbol);
      printf ("  { \"%s\", %s, TARGET_%s },\n", t->symbol, t->symbol, t->symbol);
      printf ("#endif\n");
    }
  printf ("  { 0, -1, -1 }\n");
  printf ("};\n\n");
}

int
main (int argc, char *argv[])
{
  if (argc != 2)
    abort ();

  if (strcmp (argv[1], "-h") == 0)
    gen_targ_vals_h ();
  else if (strcmp (argv[1], "-c") == 0)
    gen_targ_map_c ();
  else
    abort ();

  exit (0);
}
