SUMMARY = "The GTK+ 3 port of the wxWidgets library"
DESCRIPTION = "wxBase is a collection of C++ classes providing basic data structures (strings, \
lists, arrays), portable wrappers around many OS-specific funstions (file \
operations, time/date manipulations, threads, processes, sockets, shared \
library loading) as well as other utility classes (streams, archive and \
compression). wxBase currently supports Win32, most Unix variants (Linux, \
FreeBSD, Solaris, HP-UX) and MacOS X (Carbon and Mach-0)."
SECTION = "graphic"
LICENSE = "wxWindows"
LIC_FILES_CHKSUM = "file://docs/licence.txt;md5=18346072db6eb834b6edbd2cdc4f109b"
PR = "r0"

SRC_URI = "svn://svn.wxwidgets.org/svn/wx/wxWidgets/tags/;module=WX_2_9_5;protocol=http \
           file://tiff_m4_acinclude_remove_AX_CHECK_GL.patch \
           file://ax_check_gl.m4 \
           "

# for 'ac_raf_func_which_getservbyname_r.m4'
SRC_URI[sha256sum] = "0099a0818673ccfea006fc1bbce98693e000d1385721b3a04d27cbbe16b043ff"
# for 'gtk.m4'
SRC_URI[sha256sum] = "4d1af176791f63e35b879b470b2d391e30ef7135a37c47571122df8134ca3114"

S = "${WORKDIR}/WX_2_9_5"
SRCREV_default_pn-${PN} = "74573"

RDEPENDS_${PN} = "gtk+3"

DEPENDS = "cppunit libsdl gtk+ gtk+3 autoconf-archive"

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg"

cp_macro () {
        cp ${WORKDIR}/ax_check_gl.m4 ${S}/src/tiff/m4/
}

do_patch_append () {
    bb.build.exec_func('cp_macro', d)
}

do_configure () {
        ${S}/autogen.sh

        cd ${S}/src/tiff/
        sed -i "5s/^/#/" ${S}/src/tiff/autogen.sh
        ${S}/src/tiff/autogen.sh
        cd ${S}
        
        ${S}/configure \
                --build=${BUILD_SYS} \
		--host=${HOST_SYS} \
		--target=${TARGET_SYS} \
		--prefix=${prefix} \
		--exec_prefix=${exec_prefix} \
		--bindir=${bindir} \
		--sbindir=${sbindir} \
		--libexecdir=${libexecdir} \
		--datadir=${datadir} \
		--sysconfdir=${sysconfdir} \
		--sharedstatedir=${sharedstatedir} \
		--localstatedir=${localstatedir} \
		--libdir=${libdir} \
		--includedir=${includedir} \
		--oldincludedir=${oldincludedir} \
		--infodir=${infodir} \
		--mandir=${mandir} \
		--disable-silent-rules \
                --disable-dependency-tracking \
                --with-libtool-sysroot=${sysroot} \
                --with-gtk=3 \
                --enable-utf8 \
                --enable-stl \
                --disable-display \
                --disable-mediactrl \
                --disable-uiactionsim \
                --disable-webview \
                --disable-gtktest \
                --disable-sdltest \
                --disable-debug_flag
}

do_compile () {
        cd ${S}
        make ${PARALLEL_MAKE}
}

do_install () {
        make install DESTDIR="${D}"
}

FILES_${PN} = "${bindir}/*"
FILES_${PN}-dev = " ${libdir}/wx/* ${libdir}/libwx* ${datadir}/bakefile/* ${datadir}/aclocal/*"
FILES_${PN}-dbg = "${libdir}/.debug/libwx* ${prefix}/src/* ${includedir}/* ${bindir}/.debug/*"