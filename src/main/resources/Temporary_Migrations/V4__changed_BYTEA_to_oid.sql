ALTER TABLE public.user_general
    ALTER COLUMN cv TYPE oid USING cv::oid;

ALTER TABLE public.user_general
    ALTER COLUMN portfolio TYPE oid USING portfolio::oid;