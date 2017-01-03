# Copyright (C) 2013-2015 Freescale Semiconductor

DESCRIPTION = "U-Boot provided by Freescale with focus on  i.MX reference boards."
require recipes-bsp/u-boot/u-boot.inc
require u-boot-tinto.inc

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCBRANCH = "Tinto_2016.03"

S = "${WORKDIR}/git"

inherit fsl-u-boot-localversion

LOCALVERSION ?= "-${SRCBRANCH}"

CSTDIR = "${THISDIR}/../cst"

# for signed u-boot
do_compile_append() {
	if [ "${ENABLED_SECURE_BOOT}" = "yes" ] && [ -d ${CSTDIR} ]; then
		install ${S}/${config}/u-boot-${type}.${UBOOT_SUFFIX} ${CSTDIR}/u-boot/u-boot.imx
		cd ${CSTDIR}/u-boot
		./mk_secure_uboot
		# ovewrite source binary
		install ${CSTDIR}/u-boot/u-boot-signed-pad.imx ${S}/${config}/u-boot-${type}.${UBOOT_SUFFIX}
	fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx6|mx7|mx6ul)"
