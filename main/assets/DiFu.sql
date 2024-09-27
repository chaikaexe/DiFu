--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: achievements; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.achievements (
    achievement_id integer NOT NULL,
    user_id integer,
    achievement_name character varying(255),
    completed boolean DEFAULT false
);


ALTER TABLE public.achievements OWNER TO postgres;

--
-- Name: achievements_achievement_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.achievements_achievement_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.achievements_achievement_id_seq OWNER TO postgres;

--
-- Name: achievements_achievement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.achievements_achievement_id_seq OWNED BY public.achievements.achievement_id;


--
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
    category_id integer NOT NULL,
    category_name character varying(100) NOT NULL
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- Name: categories_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categories_category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categories_category_id_seq OWNER TO postgres;

--
-- Name: categories_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categories_category_id_seq OWNED BY public.categories.category_id;


--
-- Name: favorites; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.favorites (
    favorite_id integer NOT NULL,
    place_id integer,
    user_id integer
);


ALTER TABLE public.favorites OWNER TO postgres;

--
-- Name: favorites_favorite_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.favorites_favorite_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.favorites_favorite_id_seq OWNER TO postgres;

--
-- Name: favorites_favorite_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.favorites_favorite_id_seq OWNED BY public.favorites.favorite_id;


--
-- Name: places; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.places (
    place_id integer NOT NULL,
    place_name character varying(255) NOT NULL,
    category_id integer,
    photo text,
    address character varying(255),
    contact_info character varying(255),
    description text,
    rating double precision,
    is_favorite boolean DEFAULT false,
    working_hours character varying(100),
    CONSTRAINT places_rating_check CHECK (((rating >= (0)::double precision) AND (rating <= (5)::double precision)))
);


ALTER TABLE public.places OWNER TO postgres;

--
-- Name: places_place_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.places_place_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.places_place_id_seq OWNER TO postgres;

--
-- Name: places_place_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.places_place_id_seq OWNED BY public.places.place_id;


--
-- Name: recommendations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recommendations (
    recommendation_id integer NOT NULL,
    place_id integer,
    rating double precision,
    CONSTRAINT recommendations_rating_check CHECK (((rating >= (0)::double precision) AND (rating <= (5)::double precision)))
);


ALTER TABLE public.recommendations OWNER TO postgres;

--
-- Name: recommendations_recommendation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.recommendations_recommendation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.recommendations_recommendation_id_seq OWNER TO postgres;

--
-- Name: recommendations_recommendation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.recommendations_recommendation_id_seq OWNED BY public.recommendations.recommendation_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    username character varying(100) NOT NULL,
    password character varying(100) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_user_id_seq OWNER TO postgres;

--
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- Name: achievements achievement_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.achievements ALTER COLUMN achievement_id SET DEFAULT nextval('public.achievements_achievement_id_seq'::regclass);


--
-- Name: categories category_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories ALTER COLUMN category_id SET DEFAULT nextval('public.categories_category_id_seq'::regclass);


--
-- Name: favorites favorite_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorites ALTER COLUMN favorite_id SET DEFAULT nextval('public.favorites_favorite_id_seq'::regclass);


--
-- Name: places place_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.places ALTER COLUMN place_id SET DEFAULT nextval('public.places_place_id_seq'::regclass);


--
-- Name: recommendations recommendation_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recommendations ALTER COLUMN recommendation_id SET DEFAULT nextval('public.recommendations_recommendation_id_seq'::regclass);


--
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- Data for Name: achievements; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.achievements (achievement_id, user_id, achievement_name, completed) FROM stdin;
\.


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categories (category_id, category_name) FROM stdin;
1	Активный отдых
2	Спортивный зал
3	Здоровое питание
\.


--
-- Data for Name: favorites; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.favorites (favorite_id, place_id, user_id) FROM stdin;
\.


--
-- Data for Name: places; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.places (place_id, place_name, category_id, photo, address, contact_info, description, rating, is_favorite, working_hours) FROM stdin;
2	ProSport	2	\N	Ул. Стахановская 1	+79226688022	Спортивный тренажерный зал, Спортивный клуб, секция, Солярий, Массажный салон	4.9	f	8:00 – 22:00
3	Gym Station	2	\N	Ул. Воровского 43	+7 (909) 716-13-69	Фитнес-клуб	4.8	f	6:30 – 23:00
4	Spartak	2	\N	Ул. Октябрьский проспект 149	+7 (8332) 57-79-02	Фитнес-клуб, Спортивный комплекс, Спортивный клуб, секция, Бассейн, сауна	4.7	f	7:00 – 22:00
5	Arena Record gym	2	\N	Ул. Калинина 38Д	+7832440266	Фитнес-клуб	4.9	f	7:00 – 22:00
6	Море парк	2	\N	Ул. Некрасова 39	+7832444999	Фитнес клуб, Бассейн, баня	5	f	6:00 – 22:00
7	Modo Gym	2	\N	Ул. Воровского, 102А	+7 (8332) 74-68-68	Спортивный комплекс, спортивный клуб, секция	4.9	f	7:00 – 22:00
8	Fitness family time	2	\N	Ул. Солнечная 25Б	+7 (8332) 74-68-68	Бассейн спортивный тренажёрный зал, фитнес-клуб	5	f	7:00 – 22:00
9	Южный	1	\N	Ул. Агрономическая 7	+7 (912) 734-44-79	Теннисный корт, спортивный комплекс	4.9	f	7:00 – 22:00
10	Монро-Рассвет	2	\N	Ул. Воровского, 102А	+7 (8332) 22-20-04	Фитнес клуб	4.7	f	8:30 – 20:00
1	Ski Club	1	\N	ул. Подгорная 15	+7 (8332) 73-96-97	Спортивный комплекс, лыжная база, спортивно-развлекательная база в Кирове	4.8	f	10:00 – 21:00
11	Ё/батон	3	\N	ул. Воровского, 114 ТЦ Атлант, этаж 2	+7 (982) 389-43-49	Диетические и диабетические продукты, спортивное питание	4.1	f	10:00 – 20:00
12	Eat For Day	3	\N	ул. Герцена, 88/1 этаж 1	+7 (922) 947-74-07	Доставка еды и обедов	4.4	f	09:00 – 20:00
13	Siberian Wellness	3	\N	Московская ул., 25А	+7 (8332) 38-31-10	Фитопродукция, БАДы, спортивное питание	5	f	10:00 – 20:00
14	Живое масло	3	\N	Милицейская ул., 31	+7 (922) 955-53-20	Диетические и диабетические продукты, магазин чая	4.4	f	08:00 – 16:00
15	Полезные продукты	3	\N	ул. Екатерины Кочкиной, 3Б	+7 (8332) 69-95-51	Диетические и диабетические продукты, фитопродукция, БАДы	4.1	f	09:00 – 20:00
16	Чайхана 84/46	3	\N	ул. Карла Маркса, 84А	+7 (8332) 41-84-46	Кафе, ресторан	4.6	f	11:00 – 00:00
17	Брокколи	3	\N	ул. Горбачёва, 16	+7 (8332) 21-75-50	Кафе, доставка еды и обедов, кофейня	4.4	f	11:00 – 21:00
18	Сулугуни	3	\N	ул. Екатерины Кочкиной, 3Б, этаж 2	+7 (8332) 44-00-70	Кафе	5	f	10:55 – 23:00
19	Самое популярное кафе Восток	3	\N	ул. Свободы, 11	8 (800) 700-00-25	Кафе, ресторан	4.4	f	12:00 – 03:00
20	Царское село	3	\N	Октябрьский просп., 125	+7 (8332) 40-53-43	Ресторан, доставка еды и обедов, кафе	5	f	08:30 – 23:00
\.


--
-- Data for Name: recommendations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.recommendations (recommendation_id, place_id, rating) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, username, password) FROM stdin;
\.


--
-- Name: achievements_achievement_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.achievements_achievement_id_seq', 1, false);


--
-- Name: categories_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categories_category_id_seq', 1, false);


--
-- Name: favorites_favorite_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.favorites_favorite_id_seq', 1, false);


--
-- Name: places_place_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.places_place_id_seq', 1, false);


--
-- Name: recommendations_recommendation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.recommendations_recommendation_id_seq', 1, false);


--
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 1, false);


--
-- Name: achievements achievements_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.achievements
    ADD CONSTRAINT achievements_pkey PRIMARY KEY (achievement_id);


--
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (category_id);


--
-- Name: favorites favorites_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_pkey PRIMARY KEY (favorite_id);


--
-- Name: places places_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.places
    ADD CONSTRAINT places_pkey PRIMARY KEY (place_id);


--
-- Name: recommendations recommendations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_pkey PRIMARY KEY (recommendation_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: achievements achievements_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.achievements
    ADD CONSTRAINT achievements_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: favorites favorites_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: favorites fk_favorites_place; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT fk_favorites_place FOREIGN KEY (place_id) REFERENCES public.places(place_id) ON DELETE CASCADE;


--
-- Name: recommendations fk_recommendations_place; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT fk_recommendations_place FOREIGN KEY (place_id) REFERENCES public.places(place_id) ON DELETE CASCADE;


--
-- Name: places places_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.places
    ADD CONSTRAINT places_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.categories(category_id);


--
-- PostgreSQL database dump complete
--

