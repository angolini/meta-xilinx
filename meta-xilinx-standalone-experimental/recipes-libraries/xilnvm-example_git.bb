inherit esw deploy

ESW_COMPONENT_SRC = "/lib/sw_services/xilnvm/examples/"

DEPENDS += "xilnvm"

do_configure_prepend() {
    cd ${S}
    lopper.py ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 memory.ld ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
}

ESW_CUSTOM_LINKER_FILE ?= "None"
EXTRA_OECMAKE = "-DCUSTOM_LINKER_FILE=${@d.getVar('ESW_CUSTOM_LINKER_FILE')}"

do_install() {
    install -d ${D}/${base_libdir}/firmware
    install -m 0755  ${B}/*.elf ${D}/${base_libdir}/firmware
}

do_deploy() {
    install -d ${DEPLOYDIR}/${MACHINE}-${BPN}/
    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/*.elf ${DEPLOYDIR}/${MACHINE}-${BPN}/
}
addtask deploy before do_build after do_package

FILES_${PN} = "${base_libdir}/firmware/*.elf"