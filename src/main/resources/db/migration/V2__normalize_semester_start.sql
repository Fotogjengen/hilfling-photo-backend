-- Normalize legacy 4-digit semesters (H2024) to the 2-digit format (H24).
-- Fails on the primary key if a member has both H2024 and H24 for the same semester; resolve that by hand.

UPDATE photo_gang_banger_to_position
SET semester_start = upper(left(trim(semester_start), 1)) || right(trim(semester_start), 2)
WHERE trim(semester_start) ~* '^[VH]\d{4}$';

UPDATE photo_gang_banger_to_position
SET semester_end = upper(left(trim(semester_end), 1)) || right(trim(semester_end), 2)
WHERE trim(semester_end) ~* '^[VH]\d{4}$';

UPDATE photo_gang_banger
SET semester_start = upper(left(trim(semester_start), 1)) || right(trim(semester_start), 2)
WHERE trim(semester_start) ~* '^[VH]\d{4}$';

ALTER TABLE photo_gang_banger
    ADD COLUMN is_onboarded BOOLEAN NOT NULL DEFAULT FALSE;
