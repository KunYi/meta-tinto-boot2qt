DESCRIPTION = "Braodcom AP62x2 firmware"
LICENSE = "CLOSED"

FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "file://nvram_ap62x2.txt"
SRC_URI += "file://bcm43241b4.hcd"
SRC_URI += "file://fw_bcm43241b4_ag_apsta.bin"
SRC_URI += "file://fw_bcm43241b4_ag.bin"
SRC_URI += "file://fw_bcm43241b4_ag_p2p.bin"
SRC_URI += "file://config.txt"

S = "${WORKDIR}"

do_install () {
    install -v -d ${D}/lib/firmware/brcm/
    install -m 644 nvram_ap62x2.txt ${D}/lib/firmware/brcm/
    install -m 644 bcm43241b4.hcd ${D}/lib/firmware/brcm/
    install -m 644 fw_bcm43241b4_ag_apsta.bin ${D}/lib/firmware/brcm/
    install -m 644 fw_bcm43241b4_ag.bin ${D}/lib/firmware/brcm/
    install -m 644 fw_bcm43241b4_ag_p2p.bin ${D}/lib/firmware/brcm/
    install -m 644 config.txt ${D}/lib/firmware/brcm/
}

FILES_${PN} = "/lib/firmware/brcm/"

PACKAGE_ARCH_mx6 = "${MACHINE_ARCH}"

