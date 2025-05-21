
INSERT INTO public.tenant_permission (name, verb, resource) VALUES
-- ─────────── APPLICATION ───────────
('LIST_APPLICATIONS',          'GET',    '/api/v1/tenant/application'),
('SEARCH_APPLICATIONS',        'GET',    '/api/v1/tenant/application/filter'),
('CREATE_APPLICATION',         'POST',   '/api/v1/tenant/application'),
('VIEW_APPLICATION',           'GET',    '/api/v1/tenant/application/*'),
('UPDATE_APPLICATION',         'PUT',    '/api/v1/tenant/application/*'),
('DELETE_APPLICATION',         'DELETE', '/api/v1/tenant/application/*'),

-- ─────────── CONTRACT ───────────
('LIST_CONTRACTS',             'GET',    '/api/v1/tenant/contracts'),
('SEARCH_CONTRACTS',           'GET',    '/api/v1/tenant/contracts/filter'),
('CREATE_CONTRACT',            'POST',   '/api/v1/tenant/contracts'),
('VIEW_CONTRACT',              'GET',    '/api/v1/tenant/contracts/*'),
('UPDATE_CONTRACT',            'PUT',    '/api/v1/tenant/contracts/*'),
('DELETE_CONTRACT',            'DELETE', '/api/v1/tenant/contracts/*'),

-- ─────────── DEPARTMENT ───────────
('LIST_DEPARTMENTS',           'GET',    '/api/v1/tenant/department'),
('SEARCH_DEPARTMENTS',         'GET',    '/api/v1/tenant/department/filter'),
('CREATE_DEPARTMENT',          'POST',   '/api/v1/tenant/department'),
('VIEW_DEPARTMENT',            'GET',    '/api/v1/tenant/department/*'),
('UPDATE_DEPARTMENT',          'PUT',    '/api/v1/tenant/department/*'),
('DELETE_DEPARTMENT',          'DELETE', '/api/v1/tenant/department/*'),

-- ─────────── DOCUMENT ───────────
('LIST_DOCUMENTS',             'GET',    '/api/v1/tenant/document'),
('SEARCH_DOCUMENTS',           'GET',    '/api/v1/tenant/document/filter'),
('CREATE_DOCUMENT',            'POST',   '/api/v1/tenant/document'),
('VIEW_DOCUMENT',              'GET',    '/api/v1/tenant/document/*'),
('UPDATE_DOCUMENT',            'PUT',    '/api/v1/tenant/document/*'),
('DELETE_DOCUMENT',            'DELETE', '/api/v1/tenant/document/*'),

-- ─────────── EVALUATION_FORM ───────────
('LIST_EVALUATION_FORMS',      'GET',    '/api/v1/tenant/evaluation-forms'),
('SEARCH_EVALUATION_FORMS',    'GET',    '/api/v1/tenant/evaluation-forms/filter'),
('CREATE_EVALUATION_FORM',     'POST',   '/api/v1/tenant/evaluation-forms'),
('VIEW_EVALUATION_FORM',       'GET',    '/api/v1/tenant/evaluation-forms/*'),
('UPDATE_EVALUATION_FORM',     'PUT',    '/api/v1/tenant/evaluation-forms/*'),
('DELETE_EVALUATION_FORM',     'DELETE', '/api/v1/tenant/evaluation-forms/*'),

-- ─────────── LEAVE_REQUEST ───────────
('LIST_LEAVE_REQUESTS',        'GET',    '/api/v1/tenant/leave-request'),
('SEARCH_LEAVE_REQUESTS',      'GET',    '/api/v1/tenant/leave-request/filter'),
('CREATE_LEAVE_REQUEST',       'POST',   '/api/v1/tenant/leave-request'),
('VIEW_LEAVE_REQUEST',         'GET',    '/api/v1/tenant/leave-request/*'),
('UPDATE_LEAVE_REQUEST',       'PUT',    '/api/v1/tenant/leave-request/*'),
('DELETE_LEAVE_REQUEST',       'DELETE', '/api/v1/tenant/leave-request/*'),

-- ─────────── NOTIFICATION ───────────
('LIST_NOTIFICATIONS',         'GET',    '/api/v1/tenant/notification'),
('SEARCH_NOTIFICATIONS',       'GET',    '/api/v1/tenant/notification/filter'),
('CREATE_NOTIFICATION',        'POST',   '/api/v1/tenant/notification'),
('VIEW_NOTIFICATION',          'GET',    '/api/v1/tenant/notification/*'),
('UPDATE_NOTIFICATION',        'PUT',    '/api/v1/tenant/notification/*'),
('DELETE_NOTIFICATION',        'DELETE', '/api/v1/tenant/notification/*'),

-- ─────────── PAYROLL ───────────
('LIST_PAYROLLS',              'GET',    '/api/v1/tenant/payroll'),
('SEARCH_PAYROLLS',            'GET',    '/api/v1/tenant/payroll/filter'),
('CREATE_PAYROLL',             'POST',   '/api/v1/tenant/payroll'),
('VIEW_PAYROLL',               'GET',    '/api/v1/tenant/payroll/*'),
('UPDATE_PAYROLL',             'PUT',    '/api/v1/tenant/payroll/*'),
('DELETE_PAYROLL',             'DELETE', '/api/v1/tenant/payroll/*'),

-- ─────────── PERFORMANCE_EVAL ───────────
('LIST_PERFORMANCE_EVALS',     'GET',    '/api/v1/tenant/performance-eval'),
('SEARCH_PERFORMANCE_EVALS',   'GET',    '/api/v1/tenant/performance-eval/filter'),
('CREATE_PERFORMANCE_EVAL',    'POST',   '/api/v1/tenant/performance-eval'),
('VIEW_PERFORMANCE_EVAL',      'GET',    '/api/v1/tenant/performance-eval/*'),
('UPDATE_PERFORMANCE_EVAL',    'PUT',    '/api/v1/tenant/performance-eval/*'),
('DELETE_PERFORMANCE_EVAL',    'DELETE', '/api/v1/tenant/performance-eval/*'),

-- ─────────── POSITION ───────────
('LIST_POSITIONS',             'GET',    '/api/v1/tenant/position'),
('SEARCH_POSITIONS',           'GET',    '/api/v1/tenant/position/filter'),
('CREATE_POSITION',            'POST',   '/api/v1/tenant/position'),
('VIEW_POSITION',              'GET',    '/api/v1/tenant/position/*'),
('UPDATE_POSITION',            'PUT',    '/api/v1/tenant/position/*'),
('DELETE_POSITION',            'DELETE', '/api/v1/tenant/position/*'),

-- ─────────── ROLE ───────────
('LIST_ROLES',                 'GET',    '/api/v1/tenant/role'),
('SEARCH_ROLES',               'GET',    '/api/v1/tenant/role/filter'),
('CREATE_ROLE',                'POST',   '/api/v1/tenant/role'),
('VIEW_ROLE',                  'GET',    '/api/v1/tenant/role/*'),
('UPDATE_ROLE',                'PUT',    '/api/v1/tenant/role/*'),
('DELETE_ROLE',                'DELETE', '/api/v1/tenant/role/*'),

-- ─────────── ROLE_PERMISSION ───────────
('LIST_ROLE_PERMISSIONS',      'GET',    '/api/v1/tenant/role-permission'),
('SEARCH_ROLE_PERMISSIONS',    'GET',    '/api/v1/tenant/role-permission/filter'),
('CREATE_ROLE_PERMISSION',     'POST',   '/api/v1/tenant/role-permission'),
('VIEW_ROLE_PERMISSION',       'GET',    '/api/v1/tenant/role-permission/*'),
('UPDATE_ROLE_PERMISSION',     'PUT',    '/api/v1/tenant/role-permission/*'),
('DELETE_ROLE_PERMISSION',     'DELETE', '/api/v1/tenant/role-permission/*'),

-- ─────────── USER_ROLE ───────────
('LIST_USER_ROLES',            'GET',    '/api/v1/tenant/user-role'),
('SEARCH_USER_ROLES',          'GET',    '/api/v1/tenant/user-role/filter'),
('CREATE_USER_ROLE',           'POST',   '/api/v1/tenant/user-role'),
('VIEW_USER_ROLE',             'GET',    '/api/v1/tenant/user-role/*'),
('UPDATE_USER_ROLE',           'PUT',    '/api/v1/tenant/user-role/*'),
('DELETE_USER_ROLE',           'DELETE', '/api/v1/tenant/user-role/*'),

-- ─────────── USER (USER_TENANT) ───────────
('LIST_USERS',                 'GET',    '/api/v1/tenant/user-tenant'),
('SEARCH_USERS',               'GET',    '/api/v1/tenant/user-tenant/filter'),
('CREATE_USER',                'POST',   '/api/v1/tenant/user-tenant'),
('VIEW_USER',                  'GET',    '/api/v1/tenant/user-tenant/*'),
('UPDATE_USER',                'PUT',    '/api/v1/tenant/user-tenant/*'),
('DELETE_USER',                'DELETE', '/api/v1/tenant/user-tenant/*'),

                                                                ('AUTHENTICATE_USER', 'POST', '/api/v1/public/user/authenticate'),
                                                                ('CHANGE_PASSWORD',   'POST', '/api/v1/public/user/change-password');