# Provides the i.MX6 Tinto project common settings

require conf/machine/include/imx-base.inc
require conf/machine/include/tune-cortexa9.inc

SERIAL_CONSOLES = "115200;ttymxc0"
UBOOT_ENTRYPOINT_mx6  = "0x12000000"
KERNEL_DEVICETREE = "imx6dl-tinto.dtb"

MACHINE_FEATURES += " pci wifi bluetooth"

IMAGE_INSTALL_append += " ap62x2-fw"
IMAGE_INSTALL_append += " brcm-patchram"
CORE_IMAGE_EXTRA_INSTALL += " u-boot-fw-utils"

KERNEL_MODULE_AUTOLOAD ?= ""
KERNEL_MODULE_AUTOLOAD += " cm36771"
KERNEL_MODULE_AUTOLOAD += " egalax_i2c"
KERNEL_MODULE_AUTOLOAD += " pn544 pn544_i2c"
KERNEL_MODULE_AUTOLOAD += " pn71xx_i2c"

