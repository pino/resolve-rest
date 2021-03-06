package br.unifor.resolve.rest.resource;

import br.unifor.resolve.rest.entity.Solution;
import br.unifor.resolve.rest.dto.SolutionUpdateFields;
import br.unifor.resolve.rest.dto.SolutionDTO;
import br.unifor.resolve.rest.entity.User;
import br.unifor.resolve.rest.repository.SolutionRepository;
import br.unifor.resolve.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@RestController
@RequestMapping("/solutions")
public class SolutionResource {

    private SolutionRepository solutionRepository;
    private UserRepository userRepository;

    @Autowired
    public SolutionResource(SolutionRepository solutionRepository, UserRepository userRepository) {
        this.solutionRepository = solutionRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = GET)
    public List<SolutionDTO> findAll(Pageable pageable) {
        List<Solution> data =
                solutionRepository.findAll(pageable).getContent();
        List<SolutionDTO> dtos = new ArrayList<>();
        for (Solution s : data) {
            dtos.add(SolutionDTO.fromSolution(s));
        }
        return dtos;
    }

    @RequestMapping(value = "/{id}", method = GET)
    public SolutionDTO findById(
            @PathVariable Long id) {
        return SolutionDTO.fromSolution(solutionRepository.findById(id));
    }

    @RequestMapping(method = POST)
    public SolutionDTO insert(@RequestBody Solution solution) {
        User author = userRepository.findById(solution.getAuthor().getId());
        solution.setAuthor(author);
        return SolutionDTO.fromSolution(solutionRepository.save(solution));
    }

    @RequestMapping(value = "/{id}", method = PATCH)
    public SolutionDTO update(@RequestBody SolutionUpdateFields solution,
                             @PathVariable Long id) {
        Solution found = solutionRepository.findById(id);
        if (found == null)
            return null;
        if (solution.getContent() != null)
            found.setContent(solution.getContent());
        return SolutionDTO.fromSolution(solutionRepository.save(found));
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public Long delete(@PathVariable Long id) {
        return solutionRepository.deleteById(id);
    }

}
