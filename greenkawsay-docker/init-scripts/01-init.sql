-- Create a schema for the application
CREATE SCHEMA IF NOT EXISTS greenkawsay;

-- Configure permissions
GRANT ALL PRIVILEGES ON DATABASE greenkawsay TO postgres;
GRANT ALL PRIVILEGES ON SCHEMA greenkawsay TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA greenkawsay TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA greenkawsay TO postgres;

-- Install extension for UUIDs in greenkawsay schema
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA greenkawsay;
