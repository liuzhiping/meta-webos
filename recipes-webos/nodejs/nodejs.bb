# (c) Copyright 2012  Hewlett-Packard Development Company, L.P.

DESCRIPTION = "Node.js event-based server-side Javascript engine"
HOMEPAGE = "http://nodejs.org"
LICENSE = "MIT & Zlib"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d6237f3a840aef5b7880fb4e49eecfe5"
SECTION = "devel"

# We want node_crypto support
DEPENDS = "openssl"

PR = "r0"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_pkgconfig

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO}/${PN};tag=${WEBOS_GIT_TAG};protocol=git"
S = "${WORKDIR}/git"

# Pass in the portion before the hyphen, which is expected to have 3 dot-separated fields
NODEJS_COMPONENT_VERSION = "${@'${WEBOS_COMPONENT_VERSION}'.split('-')[0]}"

EXTRA_OECMAKE += "-DNODEJS_COMPONENT_VERSION:STRING=${NODEJS_COMPONENT_VERSION} -DTARGET_CORE_OS:STRING=rockhopper"

OECMAKE_C_FLAGS += "-fsigned-char"
OECMAKE_CXX_FLAGS += "-fsigned-char"

# XXX Temporarily add symlinks from the old locations until everything else is changed:
FILES_${PN} += "/usr/palm/nodejs/*" 
do_install_append() {
    install -d ${D}/usr/palm/nodejs
    ln -sfn ${bindir}/node ${D}/usr/palm/nodejs/node
    # /bin/node is only needed by a handful of test scripts, which aren't (yet) part of Open webOS
    # install -d ${D}/bin
    # ln -sfn ${bindir}/node ${D}${base_bindir}/node

    # XXX pmnetproxyservice isn't part of Open webOS (yet) => don't stage things only used by it:
    #
    # XXX Temporary until pmnetproxyservice is changed to search under ${D}${includedir}/node/v8
    # install -d ${D}${includedir}
    # install -m 0444 deps/v8/include/* ${D}${includedir}
    #
    # oe_libinstall -a -C ${OECMAKE_BUILDPATH}/deps/v8 libv8 ${D}${libdir}
}
