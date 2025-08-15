-- Seed data for Trackly

-- Insert sample users
INSERT INTO users (user_id, full_name, email, password_hash)
VALUES
    (uuid_generate_v4(), 'Alice Johnson', 'alice@example.com', 'hashedpassword1'),
    (uuid_generate_v4(), 'Bob Smith', 'bob@example.com', 'hashedpassword2');

-- Insert sample job applications
INSERT INTO job_applications (job_id, user_id, company_name, job_title, job_location, job_url, application_date, status)
SELECT uuid_generate_v4(), u.user_id, 'TechCorp', 'Backend Developer', 'Remote', 'https://techcorp.com/jobs/1', '2025-07-15', 'Applied'
FROM users u WHERE u.email = 'alice@example.com';

INSERT INTO job_applications (job_id, user_id, company_name, job_title, job_location, job_url, application_date, status)
SELECT uuid_generate_v4(), u.user_id, 'InnoSoft', 'Frontend Engineer', 'New York, NY', 'https://innosoft.com/jobs/2', '2025-07-10', 'Interviewing'
FROM users u WHERE u.email = 'bob@example.com';

-- Insert sample notes
INSERT INTO notes (note_id, job_id, content)
SELECT uuid_generate_v4(), j.job_id, 'Followed up via email on 2025-07-20'
FROM job_applications j JOIN users u ON j.user_id = u.user_id
WHERE u.email = 'alice@example.com';

-- Insert sample tasks
INSERT INTO tasks (task_id, job_id, description, due_date)
SELECT uuid_generate_v4(), j.job_id, 'Prepare for technical interview', '2025-07-25'
FROM job_applications j JOIN users u ON j.user_id = u.user_id
WHERE u.email = 'bob@example.com';

-- Insert sample documents
INSERT INTO documents (document_id, user_id, job_id, file_name, file_path)
SELECT uuid_generate_v4(), u.user_id, j.job_id, 'resume_alice.pdf', '/uploads/resume_alice.pdf'
FROM job_applications j JOIN users u ON j.user_id = u.user_id
WHERE u.email = 'alice@example.com';
