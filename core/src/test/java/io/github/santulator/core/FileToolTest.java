package io.github.santulator.core;

import io.github.santulator.test.core.TestFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileToolTest {
    private static final String ERROR_TEMPLATE = "Error %s";
    private TestFileManager manager;

    private Path base;

    private Path existingFile;

    private Path newFile;

    private Path subDirectory;

    @BeforeEach
    public void setUp() throws Exception {
        manager = new TestFileManager(getClass());
        base = manager.getDirectory();
        existingFile = manager.addFile("existingFile");
        Files.createFile(existingFile);
        newFile = manager.addFile("newFile");
        subDirectory = manager.addFile("directory");
        Files.createDirectories(subDirectory);
    }

    @AfterEach
    public void tearDown() throws Exception {
        manager.cleanup();
    }

    @Test
    public void testEnsureDirectoryExistsWithDirectory() {
        FileTool.ensureDirectoryExists(existingFile, ERROR_TEMPLATE);

        assertTrue(Files.isDirectory(base));
    }

    @Test
    public void testEnsureDirectoryExistsWithError() {
        Path impossible = existingFile.resolve("file");

        assertThrows(SantaException.class, () -> FileTool.ensureDirectoryExists(impossible, ERROR_TEMPLATE));
    }

    @Test
    public void testEnsureDirectoryExistsWithoutBase() {
        Path file = Paths.get("file");
        FileTool.ensureDirectoryExists(file, ERROR_TEMPLATE);
    }

    @Test
    public void testWriteMinimalJsonSuccess() throws Exception {
        FileTool.writeMinimalJson(newFile, ERROR_TEMPLATE);

        List<String> result = Files.readAllLines(newFile);
        List<String> expected = Collections.singletonList("{}");

        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinimalJsonFailure() {
        assertThrows(SantaException.class, () -> FileTool.writeMinimalJson(subDirectory, ERROR_TEMPLATE));
    }

    @Test
    public void testReadThenWriteJson() {
        FileToolTestBean bean = new FileToolTestBean("left", "right");

        FileTool.writeAsJson(newFile, bean, ERROR_TEMPLATE);
        FileToolTestBean result = FileTool.readFromJson(FileToolTestBean.class, newFile, ERROR_TEMPLATE);

        assertEquals(bean, result);
    }

    @Test
    public void testWriteAsJsonWithError() {
        assertThrows(SantaException.class, () -> FileTool.writeAsJson(subDirectory, new FileToolTestBean(), ERROR_TEMPLATE));
    }

    @Test
    public void testReadFromJsonWithError() {
        assertThrows(SantaException.class, () -> FileTool.readFromJson(FileToolTestBean.class, subDirectory, ERROR_TEMPLATE));
    }
}
