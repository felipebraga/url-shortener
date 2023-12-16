INSERT INTO public.users (full_name, email, username, "password")
    VALUES ('Felipe Braga', 'user.demo@example.com', 'felipeab', '{bcrypt}$2a$10$4aEMuYiqwF7NteNMvf/Vv.cQ0NJiaXn.FIsWhGYIFkP7r/5WheUmK'); --'pass-felipe'

-- Extra Users
INSERT INTO public.users (full_name, email, username, "password")
VALUES ('Karianne Gottlieb', 'karianne.gottlieb0@yahoo.com', 'karianne.gottlieb0', '{bcrypt}$2a$10$i.HiOiiVgIEbPGVU5VlSZeldaToyXlZsSxW5BKLYnMTbj/XfNbsRS'), --'24oLsWu4'
       ('Judah MacGyver', 'judah97@gmail.com', 'judah97', '{bcrypt}$2a$10$Rz4oQCWJGXe7VNU/osQKzea9WAmqrYS4cD3lVg8TThuHkWOmPyNGm'), --'n1nJcznx'
       ('Ethel Daniel', 'ethel53@gmail.com', 'ethel53', '{bcrypt}$2a$10$VY7fie9axFxSsPswSW1SKen4tMvFSEX7K002FI3QPPWg0M2VOkimu'), --'8lXYNZEJ'
       ('Georgianna Bashirian Jr.', 'georgianna.bashirian55@hotmail.com', 'georgianna.bashirian55', '{bcrypt}$2a$10$D2GTG/oLqZYCp4nJhwo/uOLfOy4JfwILCt7uGt28nYKMJWG0w4VAa'), --'uEWSg1Xr'
       ('Percy Schroeder', 'percy_schroeder@gmail.com', 'percy_schroeder', '{bcrypt}$2a$10$ZNUKzH2YoLVd7X6qDIpwju0QS.8czfi5gZy3DiRrS5LeAUUhCJUaS'), --'DWY5ySzI'
       ('Leanne Howell-Connelly', 'leanne.howell-connelly73@gmail.com', 'leanne.howell-connelly73', '{bcrypt}$2a$10$f1xHdCLHXeIHgxxutyVRweIL.T0Gd4aeTvXsP/uG9l0oCaU6R28Ry'), --'8q1UuYfV'
       ('Eusebio Bosco', 'eusebio_bosco@gmail.com', 'eusebio_bosco', '{bcrypt}$2a$10$IkpTC4Yaj6kCQwPLEqLL/uy2tWdXfId9CETZvcFPYJ67TMnDSdDBK'), --'JSNF7ymh'
       ('Celestino Kuvalis', 'celestino29@yahoo.com', 'celestino29', '{bcrypt}$2a$10$R7pIwD2qzg4OPotV2DmJFOw//DB7yZutZ/CkphTKa/UtlPDMwIIAy'), --'yELLW_9C'
       ('Lucio Dooley', 'lucio.dooley77@gmail.com', 'lucio.dooley77', '{bcrypt}$2a$10$PYnH6ZCoXRY5OQt7WmJToeZoxUHmYtHH32zsiQdMEM0OvSd3qtZ.C'), --'nZfcrYrJ'
       ('Cassie Bahringer', 'cassie_bahringer95@gmail.com', 'cassie_bahringer95', '{bcrypt}$2a$10$KZwQy1asoq2Xcm6J9rahOOEq/qmMMwb.OF3iNk0SfllPGb8JUFK7y'); --'E5CUJLcU'

-- UPDATE public.users SET "password"=crypt("password", gen_salt('bf', 10));
