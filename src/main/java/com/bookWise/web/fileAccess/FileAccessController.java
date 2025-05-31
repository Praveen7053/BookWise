package com.bookWise.web.fileAccess;

import com.bookWise.dao.BookWiseDAO;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@Transactional(readOnly = true)
public class FileAccessController extends AbstractController {

    private BookWiseDAO bookWiseDAO;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String pdfPath = request.getParameter("pdfPath");

        OutputStream out = response.getOutputStream();
        //File file = null;

        if (StringUtils.isNotBlank(pdfPath)) {
            File file = new File("/usr/local/bookWiseFile/BookUpload/" + pdfPath);
            if (file.exists()) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
                FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            }
        }

        return null;
    }

}
