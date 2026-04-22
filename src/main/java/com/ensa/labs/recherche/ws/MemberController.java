package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.MemberDTO;
import com.ensa.labs.recherche.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberDTO> findAll() { return memberService.findAll(); }

    @GetMapping("/{id}")
    public MemberDTO findById(@PathVariable String id) { return memberService.findById(id); }

    @GetMapping("/lab/{labAcronym}")
    public List<MemberDTO> findByLabAcronym(@PathVariable String labAcronym) { return memberService.findByLabAcronym(labAcronym); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDTO create(@RequestBody MemberDTO dto) { return memberService.create(dto); }

    @PutMapping("/{id}")
    public MemberDTO update(@PathVariable String id, @RequestBody MemberDTO dto) { return memberService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { memberService.delete(id); }
}
