

drop table if exists jukebox.jb_artists;

create table jukebox.jb_artists  (
	id serial NOT NULL, 
	artist_name varchar(30) NOT NULL,
	shared_folder varchar(30),
	owner_id bigint,
	date_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

drop table if exists jukebox.jb_albums;

create table jukebox.jb_albums  (
	id serial NOT NULL, 
	album_name varchar(30) NOT NULL,
	artist_id bigint, 
	shared_folder varchar(30),
	owner_id bigint,
	date_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

drop table if exists jukebox.jb_tracks;

create table jukebox.jb_tracks  (
	id serial NOT NULL, 
	track_name varchar(30) NOT NULL,
	album_id bigint, 
	artist_id bigint,
	shared_folder varchar(30),	
	owner_id bigint,
	date_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

drop table if exists jukebox.jb_users;

create table jukebox.jb_users  (
	id serial NOT NULL, 
	username varchar(30) NOT NULL,
	shared_folder varchar(30) NOT NULL,
	date_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

drop table if exists jukebox.jb_network_paths;

create table jukebox.jb_network_paths (
    id serial NOT NULL, 
    owner_id bigint NOT NULL,
    shared_folder varchar(30) NOT NULL,
    date_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TYPE request_status AS ENUM ('PLAYING', 'QUEUED', 'PLAYED');

create table jb_playlist (
    request_id serial NOT NULL, 
    track_id bigint NOT NULL,
    status request_status DEFAULT 'QUEUED',
    requested_by bigint, 
    date_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (request_id)
);

ALTER TABLE jukebox.jb_tracks
ADD CONSTRAINT fk_album_id 
FOREIGN KEY (album_id) 
REFERENCES jukebox.jb_albums(id);

ALTER TABLE jukebox.jb_tracks
ADD CONSTRAINT fk_artist_id 
FOREIGN KEY (artist_id) 
REFERENCES jukebox.jb_artists(id);

ALTER TABLE jukebox.jb_albums
ADD CONSTRAINT fk_artist_id 
FOREIGN KEY (artist_id) 
REFERENCES jukebox.jb_artists(id);

CREATE INDEX jb_track_album_idx ON jukebox.jb_tracks (album_id);
CREATE INDEX jb_track_artist_idx ON jukebox.jb_tracks (artist_id);

alter table jb_artists add column found_on_last_load bool default false;
alter table jb_albums add column found_on_last_load bool default false;
alter table jb_tracks add column found_on_last_load bool default false;

create table jb_users (
    user_id serial NOT NULL, 
    username varchar(30) NOT NULL, 
    shared_folder varchar(30) NOT NULL,
    date_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

insert into jb_users (username, shared_folder) values ('chris', 'd:\mp3');