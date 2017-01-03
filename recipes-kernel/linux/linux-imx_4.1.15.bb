# linux-yocto-custom.bb:
#
#   An example kernel recipe that uses the linux-yocto and oe-core
#   kernel classes to apply a subset of yocto kernel management to git
#   managed kernel repositories.
#
#   To use linux-yocto-custom in your layer, create a
#   linux-yocto-custom.bbappend file containing at least the following
#   lines:
#
#     FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
#     COMPATIBLE_MACHINE_yourmachine = "yourmachine"
#
#   You must also provide a Linux kernel configuration. The most direct
#   method is to copy your .config to files/defconfig in your layer,
#   in the same directory as the bbappend and add file://defconfig to
#   your SRC_URI.
#
#   To use the yocto kernel tooling to generate a BSP configuration
#   using modular configuration fragments, see the yocto-bsp and
#   yocto-kernel tools documentation.
#
# Warning:
#
#   Building this example without providing a defconfig or BSP
#   configuration will result in build or boot errors. This is not a
#   bug.
#
#
# Notes:
#
#   patches: patches can be merged into to the source git tree itself,
#            added via the SRC_URI, or controlled via a BSP
#            configuration.
#
#   defconfig: When a defconfig is provided, the linux-yocto configuration
#              uses the filename as a trigger to use a 'allnoconfig' baseline
#              before merging the defconfig into the build. 
#
#              If the defconfig file was created with make_savedefconfig, 
#              not all options are specified, and should be restored with their
#              defaults, not set to 'n'. To properly expand a defconfig like
#              this, specify: KCONFIG_MODE="--alldefconfig" in the kernel
#              recipe.
#   
#   example configuration addition:
#            SRC_URI += "file://smp.cfg"
#   example patch addition (for kernel v3.4 only):
#            SRC_URI += "file://0001-linux-version-tweak.patch
#   example feature addition (for kernel v3.4 only):
#            SRC_URI += "file://feature.scc"
#

require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

# Override SRC_URI in a bbappend file to point at a different source
# tree if you do not want to build from Linus' tree.
LINUX_VERSION ?= "4.1.15"
LINUX_VERSION_EXTENSION ?= "-tinto"

# Override SRCREV to point to a different commit in a bbappend file to
# build a different release of the Linux kernel.
SRCREV = "afbca0a47b51c67df4333d36f087a80721327f36"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
DEPENDS += "lzop-native bc-native"

SRC_BRANCH = "Tinto"
KERNEL_SRC = "git://github.com/UBIQCONN/imx6-linux-4.1.15.git;protocol=http;branch=${SRC_BRANCH}"
SRC_URI = "${KERNEL_SRC};"

DEFAULT_PREFERENCE = "1"

CSTDIR = "${BSPDIR}/sources/meta-tinto/recipes-bsp/cst/zImage"

DO_CONFIG_V7_COPY = "no"
DO_CONFIG_V7_COPY_mx6 = "yes"
DO_CONFIG_V7_COPY_mx6ul = "yes"
DO_CONFIG_V7_COPY_mx7 = "yes"


addtask copy_defconfig after do_patch before do_preconfigure #do_configure

do_copy_defconfig () {
	if [ ${DO_CONFIG_V7_COPY} = "yes" ]; then
		# copy latest imx_v7_defconfig to use for mx6, mx6ul and mx7
		mkdir -p ${B}
		cp ${S}/arch/arm/configs/imx_v7_defconfig ${B}/.config
		cp ${S}/arch/arm/configs/imx_v7_defconfig ${B}/../defconfig
	fi
}

kernel_do_compile_append() {
	if test "${KERNEL_IMAGETYPE}" = "zImage"; then
		if [ "${ENABLED_SECURE_BOOT}" = "yes" ] && [ -d ${CSTDIR} ]; then
			install -m 0644 ${KERNEL_OUTPUT} ${CSTDIR}
			cd ${CSTDIR}
			./mk_secure_zimage "${UBOOT_LOADADDRESS}"
			install -m 0644 "${CSTDIR}/zImage-signed-pad.bin" ${S}/${KERNEL_OUTPUT}
			install -m 0644 "${CSTDIR}/zImage-signed-pad.bin" ${B}/${KERNEL_OUTPUT}
			cd ${B}
		fi
	fi
}

#
# Override COMPATIBLE_MACHINE to include your machine in a bbappend
# file. Leaving it empty here ensures an early explicit build failure.
COMPATIBLE_MACHINE = "(mx6|mx6ul|mx7)"
