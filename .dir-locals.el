((less-css-mode . ((eval . (add-hook 'after-save-hook
                                     (lambda ()
                                       (let ((default-directory (file-truename (locate-dominating-file buffer-file-name ".git"))))
                                         (compile "make css")))
                                     nil
                                     t)))))
