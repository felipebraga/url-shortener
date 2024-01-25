INSERT INTO public.users (full_name, email, username, "password")
    VALUES ('Filius Flitwick', 'filius.flitwick@hogwarts.edu', 'flitwick', '{bcrypt}$2a$10$6PD0HgO.jj0baBSaiSVitO9rDSTbWGP8XX060bgFjKhqEfMLaA0da'); --'alohomora'

-- UPDATE public.users SET "password"=crypt("password", gen_salt('bf', 10));
