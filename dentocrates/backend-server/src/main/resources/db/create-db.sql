DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'dentocrates') THEN
            CREATE DATABASE dentocrates;
        END IF;
    END $$;
