package com.github.wolneiacordeiro.bichochicapi.controller;

import com.github.wolneiacordeiro.bichochicapi.Repository.ProdutoRepository;
import com.github.wolneiacordeiro.bichochicapi.entity.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Produto getProdutoById(@PathVariable String id) {
        return produtoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Produto criarProduto(
            @RequestParam("nome") String nome,
            @RequestParam("preco") double preco,
            @RequestParam("descricao") String descricao,
            @RequestParam("tamanho") String tamanho,
            @RequestParam("quantidade") int quantidade,
            @RequestParam("especie") String especie,
            @RequestParam("categoria") String categoria,
            @RequestParam("imagem") MultipartFile imagem) throws IOException {

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setDescricao(descricao);
        produto.setTamanho(tamanho);
        produto.setQuantidade(quantidade);
        produto.setEspecie(especie);
        produto.setCategoria(categoria);

        if (imagem != null && !imagem.isEmpty()) {
            String nomeArquivo = StringUtils.cleanPath(imagem.getOriginalFilename());

            String uploadDir = "./images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = imagem.getInputStream()) {
                Path filePath = uploadPath.resolve(nomeArquivo);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            produto.setImagem(nomeArquivo);
        }

        return produtoRepository.save(produto);
    }

    @PutMapping("/{id}")
    public Produto updateProduto(@PathVariable String id, @RequestParam("imagem") MultipartFile imagem, @ModelAttribute Produto produto) throws IOException {
        Produto existingProduto = produtoRepository.findById(id).orElse(null);
        if (existingProduto != null) {
            existingProduto.setNome(produto.getNome());
            existingProduto.setPreco(produto.getPreco());
            existingProduto.setDescricao(produto.getDescricao());
            existingProduto.setTamanho(produto.getTamanho());
            existingProduto.setQuantidade(produto.getQuantidade());
            existingProduto.setEspecie(produto.getEspecie());
            existingProduto.setCategoria(produto.getCategoria());

            if (imagem != null && !imagem.isEmpty()) {

                if (existingProduto.getImagem() != null) {
                    Path oldFilePath = Paths.get("./images/", existingProduto.getImagem());
                    Files.deleteIfExists(oldFilePath);
                }

                String nomeArquivo = StringUtils.cleanPath(imagem.getOriginalFilename());
                try (InputStream inputStream = imagem.getInputStream()) {
                    Path filePath = Paths.get("./images/", nomeArquivo);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                existingProduto.setImagem(nomeArquivo);
            }

            return produtoRepository.save(existingProduto);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable String id) {
        Produto existingProduto = produtoRepository.findById(id).orElse(null);
        if (existingProduto != null) {
            produtoRepository.deleteById(id);

            if (existingProduto.getImagem() != null) {
                Path filePath = Paths.get("./images/", existingProduto.getImagem());
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                }
            }

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

