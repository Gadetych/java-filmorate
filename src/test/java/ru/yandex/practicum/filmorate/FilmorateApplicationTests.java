package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.dao.BaseDBRepository;
import ru.yandex.practicum.filmorate.dao.db.UserDBRepository;
import ru.yandex.practicum.filmorate.dao.db.row.mapper.user.UserRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {
		UserDBRepository.class, UserService.class, UserMapper.class, UserRowMapper.class,
//		FilmDBRepository.class, FilmService.class, FilmMapper.class, FilmRowMapper.class,
//		GenreDBRepository.class, GenreService.class, GenreMapper.class, GenreRowMapper.class,
//		MpaDBRepository.class, MpaService.class, MpaMapper.class, MpaRowMapper.class,
		BaseDBRepository.class})
class FilmorateApplicationTests {
	private final UserDBRepository userDBRepository;
//	private final FilmDBRepository filmDBRepository;
//	private final GenreDBRepository genreDBRepository;
//	private final MpaDBRepository mpaDBRepository;

	@Test
	public void testFindUserById() {

		Optional<User> userOptional = userDBRepository.get(1);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}
}

