((less-css-mode . ((eval . (progn
                             (setq-local project-root-directory (file-truename (locate-dominating-file buffer-file-name ".git")))
                             (setq-local less-css-lessc-command (concat project-root-directory "node_modules/.bin/lessc"))))
                   (less-css-compile-at-save . t))))
