-- 사용자 더미 데이터
INSERT INTO users (id, email, name, password, created_at, is_deleted, locked, role) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'test@email.com', 'test', '$2a$12$.AmgP4Hqjw3a3ah5/Wo.AuxJoF1mMlP5y5N4ZNucrz4V.fslF1Koi', NOW(), false, false, 'USER'),
('550e8400-e29b-41d4-a716-446655440002', 'test1@email.com', 'test1', '$2a$12$.AmgP4Hqjw3a3ah5/Wo.AuxJoF1mMlP5y5N4ZNucrz4V.fslF1Koi', NOW(), false, false, 'USER');